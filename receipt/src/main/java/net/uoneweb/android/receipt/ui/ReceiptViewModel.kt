package net.uoneweb.android.receipt.ui

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import com.google.firebase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.uoneweb.android.data.ReceiptSettingDataStore
import net.uoneweb.android.gis.ui.location.Location
import net.uoneweb.android.gis.ui.map.Feature
import net.uoneweb.android.mapping.ui.ReceiptMappingEvent
import net.uoneweb.android.receipt.data.Receipt
import net.uoneweb.android.receipt.data.ReceiptMappingInfo
import net.uoneweb.android.receipt.data.ReceiptMetaData
import net.uoneweb.android.receipt.repository.ReceiptMappingInfoRepository
import net.uoneweb.android.receipt.repository.ReceiptMetaDataRepository
import net.uoneweb.android.receipt.ui.detail.ReceiptDetailUiState
import net.uoneweb.android.receipt.webapi.ChatRequest
import net.uoneweb.android.receipt.webapi.ChatResponse
import net.uoneweb.android.receipt.webapi.GeminiReceiptPrompt
import net.uoneweb.android.receipt.webapi.GeminiReceiptResponse
import net.uoneweb.android.receipt.webapi.Message
import net.uoneweb.android.receipt.webapi.OpenAiReceiptPrompt
import net.uoneweb.android.receipt.webapi.ResponseFormat
import net.uoneweb.android.receipt.webapi.RetrofitInstance
import net.uoneweb.android.receipt.webapi.SampleData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReceiptViewModel(application: Application) : AndroidViewModel(application) {
    private val receiptMetaDataRepository = ReceiptMetaDataRepository(application)
    private val receiptMappingInfoRepository = ReceiptMappingInfoRepository(application)

    private val _receiptDetailUiState = MutableStateFlow(ReceiptDetailUiState())
    val receiptDetailUiState: StateFlow<ReceiptDetailUiState> = _receiptDetailUiState.asStateFlow()

    private val settings = ReceiptSettingDataStore(getApplication())

    private val _feature: MutableState<Feature?> = mutableStateOf(null)
    val feature: State<Feature?> = _feature
    private val _loadingFeature: MutableState<Boolean> = mutableStateOf(false)
    val loadingFeature: MutableState<Boolean> = _loadingFeature
    private val _osmInfoJson: MutableState<String> = mutableStateOf("")
    val osmInfoJson: State<String> = _osmInfoJson


    init {
        this.viewModelScope.launch {
            settings.apiKeyFlow.collect { value ->
                // TODO: refactor
                RetrofitInstance.apiKey = value
            }
        }
    }

    fun saveReceiptMetaData(receiptMetaData: ReceiptMetaData) {
        viewModelScope.launch {
            val id = receiptMetaDataRepository.insert(receiptMetaData)
            _receiptDetailUiState.update {
                it.copy(
                    receipt = it.receipt.copy(id = id),
                    saved = true,
                )
            }
        }
    }

    val listReceiptMetaData: StateFlow<List<ReceiptMetaData>> = receiptMetaDataRepository.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList(),
    )

    fun listReceiptMappingInfos(): StateFlow<List<ReceiptMappingInfo>> =
        receiptMappingInfoRepository.getAll()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList(),
            )

    private val reciptMappingInfoCache = mutableMapOf<Long, StateFlow<List<ReceiptMappingInfo>>>()

    fun listReceiptMappingInfosByReceiptId(receiptId: Long): StateFlow<List<ReceiptMappingInfo>> =
        reciptMappingInfoCache.getOrPut(receiptId) {
            receiptMappingInfoRepository.getByReceiptId(receiptId)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.Lazily,
                    initialValue = emptyList(),
                )
        }


    fun select(id: Long) {
        viewModelScope.launch {
            receiptMetaDataRepository.getById(id).collect { metadata ->
                if (metadata != null) {
                    _receiptDetailUiState.update {
                        it.copy(
                            receipt = metadata,
                            saved = true,
                        )
                    }
                }
            }
        }
    }


    fun reset() {
        _osmInfoJson.value = ""
        _receiptDetailUiState.update {
            it.copy(
                selectedImageUri = null,
                uploadedImageUri = null,
                receipt = ReceiptMetaData.Empty,
                loading = false,
                saved = true,
            )
        }
    }

    fun uploadImage(localFileUri: Uri?) {
        if (localFileUri == null) {
            return
        }

        val storage = Firebase.storage
        val storageRef = storage.reference
        val imagesRef = storageRef.child("images")
        val fileRef = imagesRef.child(localFileUri.lastPathSegment!!)

        val uploadTask = fileRef.putFile(localFileUri)
        uploadTask.addOnSuccessListener {
            println("Upload successful")
        }.addOnFailureListener {
            println("Upload failed")
        }

        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            fileRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                _receiptDetailUiState.update {
                    it.copy(uploadedImageUri = downloadUri)
                }
                println("downloadUri: $downloadUri")
            } else {
                println("downloadUri: failed")
            }
        }
    }

    fun setLocation(location: Location) {
        _receiptDetailUiState.update {
            it.copy(
                location = location,
                receipt = it.receipt.copy(location = location),
                saved = false,
            )
        }

    }

    suspend fun generateJsonFromImage(localFileUri: Uri) {
        _receiptDetailUiState.update {
            it.copy(
                selectedImageUri = localFileUri,
                loading = true,
                saved = false,
            )
        }
        val contentResolver = getApplication<Application>().contentResolver
        val image = ReceiptImage(contentResolver, localFileUri)
        val imageLocation = image.location()

        val prompt = content {
            image(image.bitmap())
            text(GeminiReceiptPrompt.loadPrompt(getApplication()))
        }


        val model = Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel("gemini-2.0-flash")
        val response = model.generateContent(prompt)
        print(response.text)
        val parser = GeminiReceiptResponse(response.text)
        print(parser.json())
        _receiptDetailUiState.update {
            it.copy(
                loading = false,
                imageLocation = imageLocation,
                receipt = ReceiptMetaData(Receipt.fromJson(parser.json())),
            )
        }
    }

    fun receiptResultTest() {
        val testData = SampleData.dummyData(getApplication())
        _receiptDetailUiState.update {
            it.copy(
                receipt = ReceiptMetaData(Receipt.fromJson(testData)),
            )
        }
    }


    suspend fun generateOsmInfoFromJson(json: String, isTest: Boolean) {
        if (isTest) {
            _osmInfoJson.value = SampleData.responseSample(getApplication())
            return
        }
        val prompt = OpenAiReceiptPrompt.promptToInferOsmFeatureFromReceipt(getApplication(), json)

        val request = ChatRequest(
            //model = "gpt-4o",
            // model = "gpt-4-turbo-2024-04-09",
            model = "gpt-4.1",
            messages = listOf(
                Message(
                    role = "user",
                    content = prompt,
                ),
            ),
            response_format = ResponseFormat(type = "json_object"),
        )
        Log.i("OpenAI", "Request start")
        _loadingFeature.value = true
        RetrofitInstance.api.getChatCompletion(request).enqueue(
            object : Callback<ChatResponse> {
                override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                    if (response.isSuccessful) {
                        val chatResponse = response.body()
                        println(chatResponse)
                        chatResponse?.choices?.forEach { choice ->
                            val message = choice.message
                            println("${message.role}: ${message.content}")
                        }
                        Log.i("OpenAI", "Response successful")
                        Log.i("OpenAI", "Response: ${response.body()}")
                        val jsonString = response.body()?.choices?.get(0)?.message?.content ?: ""
                        viewModelScope.launch {
                            receiptMappingInfoRepository.insert(ReceiptMappingInfo.fromJson(jsonString))
                        }
                        _osmInfoJson.value = response.body()?.choices?.get(0)?.message?.content ?: ""
                    } else {
                        Log.e("OpenAI", "Response failed: ${response.errorBody()?.string()}")
                    }
                    _loadingFeature.value = false
                }

                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    println("Failed to get chat completion: ${t.message}")
                    _loadingFeature.value = false
                }
            },
        )
    }


    suspend fun generateOsmInfoFromReceiptAndFeature(receiptJson: String, featureJson: String, isTest: Boolean = false) {
        if (isTest) {
            return
        }
        val prompt = OpenAiReceiptPrompt.promptToInferOsmFeatureFromReceiptAndActualFeature(getApplication(), receiptJson, featureJson)
        Log.i("ReceiptViewModel", "prompt: $prompt")

        val request = ChatRequest(
            model = "gpt-4.1",
            messages = listOf(
                Message(
                    role = "user",
                    content = prompt,
                ),
            ),
            response_format = ResponseFormat(type = "json_object"),
        )
        Log.i("OpenAI", "Request start")
        _loadingFeature.value = true
        RetrofitInstance.api.getChatCompletion(request).enqueue(
            object : Callback<ChatResponse> {
                override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                    if (response.isSuccessful) {
                        val chatResponse = response.body()
                        println(chatResponse)
                        chatResponse?.choices?.forEach { choice ->
                            val message = choice.message
                            println("${message.role}: ${message.content}")
                        }
                        Log.i("OpenAI", "Response successful")
                        Log.i("OpenAI", "Response: ${response.body()}")
                        val jsonString = response.body()?.choices?.get(0)?.message?.content ?: ""
                        viewModelScope.launch {
                            receiptMappingInfoRepository.insert(ReceiptMappingInfo.fromJson(jsonString))
                        }
                        _osmInfoJson.value = response.body()?.choices?.get(0)?.message?.content ?: ""
                    } else {
                        Log.e("OpenAI", "Response failed: ${response.errorBody()?.string()}")
                    }
                    _loadingFeature.value = false
                }

                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    println("Failed to get chat completion: ${t.message}")
                    _loadingFeature.value = false
                }
            },
        )
    }

    fun onReceiptMappingEvent(event: ReceiptMappingEvent) {
        val json = if (_receiptDetailUiState.value.receipt == ReceiptMetaData.Empty) {
            SampleData.dummyData(getApplication())
        } else {
            _receiptDetailUiState.value.receipt.content.json
        }
        when (event) {
            is ReceiptMappingEvent.OnInferClicked -> {
                if (feature.value == null) {
                    viewModelScope.launch {
                        generateOsmInfoFromJson(json, false)
                    }
                } else {
                    viewModelScope.launch {
                        // TODO: feature
                        generateOsmInfoFromReceiptAndFeature(json, feature.value!!.geojson)
                    }
                }
            }

            is ReceiptMappingEvent.OnTestInferClicked -> {
                viewModelScope.launch {
                    generateOsmInfoFromJson("", true)
                }

            }
        }
    }

    fun updateCorrectionText(text: String) {
        _receiptDetailUiState.update {
            it.copy(correctionText = text)
        }
    }

    suspend fun correctReceiptInfo(correction: String) {
        val currentReceiptJson = _receiptDetailUiState.value.receipt.content.json
        val selectedImageUri = _receiptDetailUiState.value.selectedImageUri

        if (selectedImageUri == null) {
            Log.e("ReceiptViewModel", "No image selected for correction.")
            return
        }

        _receiptDetailUiState.update {
            it.copy(
                loading = true,
                correctionText = "", // Clear correction text after submission
            )
        }

        val contentResolver = getApplication<Application>().contentResolver
        val image = ReceiptImage(contentResolver, selectedImageUri)

        val prompt = content {
            image(image.bitmap())
            text(GeminiReceiptPrompt.loadCorrectionPrompt(getApplication(), currentReceiptJson, correction))
        }

        val model = Firebase.ai(backend = GenerativeBackend.googleAI())
            .generativeModel("gemini-2.0-flash")
        val response = model.generateContent(prompt)
        print(response.text)
        val parser = GeminiReceiptResponse(response.text)
        print(parser.json())
        _receiptDetailUiState.update {
            it.copy(
                loading = false,
                receipt = ReceiptMetaData(Receipt.fromJson(parser.json())),
            )
        }
    }
}

