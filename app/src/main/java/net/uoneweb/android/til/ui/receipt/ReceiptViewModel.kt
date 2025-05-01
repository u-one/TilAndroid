package net.uoneweb.android.til.ui.receipt

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.vertexAI
import kotlinx.coroutines.launch
import net.uoneweb.android.til.data.SettingsDataStore
import net.uoneweb.android.til.ui.receipt.data.Receipt
import net.uoneweb.android.til.ui.receipt.webapi.ChatRequest
import net.uoneweb.android.til.ui.receipt.webapi.ChatResponse
import net.uoneweb.android.til.ui.receipt.webapi.GeminiReceiptPrompt
import net.uoneweb.android.til.ui.receipt.webapi.GeminiReceiptResponse
import net.uoneweb.android.til.ui.receipt.webapi.Message
import net.uoneweb.android.til.ui.receipt.webapi.OpenAiReceiptPrompt
import net.uoneweb.android.til.ui.receipt.webapi.ResponseFormat
import net.uoneweb.android.til.ui.receipt.webapi.RetrofitInstance
import net.uoneweb.android.til.ui.receipt.webapi.SampleData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ReceiptViewModel(application: Application) : AndroidViewModel(application) {
    private val _uploadedFileUrl: MutableState<Uri?> = mutableStateOf(null)
    val uploadedFileUrl: State<Uri?> = _uploadedFileUrl
    private val _json: MutableState<String> = mutableStateOf("")
    val json: State<String> = _json
    private val _receipt: MutableState<Receipt> = mutableStateOf(Receipt.Empty)
    val receipt: State<Receipt> = _receipt
    private val settings = SettingsDataStore(getApplication())

    init {
        this.viewModelScope.launch {
            settings.preferenceFlow.collect { value ->
                // TODO: refactor
                RetrofitInstance.apiKey = value
            }
        }
    }

    fun reset() {
        _uploadedFileUrl.value = null
        _json.value = ""
        _receipt.value = Receipt.Empty
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
                _uploadedFileUrl.value = downloadUri
                println("downloadUri: $downloadUri")
            } else {
                println("downloadUri: failed")
            }
        }
    }

    suspend fun generateJsonFromImage(localFileUri: Uri) {
        val contentResolver = getApplication<Application>().contentResolver
        val bitmap: Bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(localFileUri))
        val prompt = content {
            image(bitmap)
            text(GeminiReceiptPrompt.loadPrompt(getApplication()))
        }

        val model = Firebase.vertexAI.generativeModel("gemini-2.0-flash")
        val response = model.generateContent(prompt)
        print(response.text)
        val parser = GeminiReceiptResponse(response.text)
        print(parser.json())

        _receipt.value = Receipt(parser.json())
    }

    fun receiptResultTest() {
        val testData = SampleData.dummyData(getApplication())
        _receipt.value = Receipt(testData)
    }


    suspend fun generateOsmInfoFromJson(json: String, isTest: Boolean) {
        if (isTest) {
            _json.value = SampleData.responseSample(getApplication())
            return
        }
        val prompt = OpenAiReceiptPrompt.loadOsmFromJsonPrompt(getApplication())
        val promptWithJson = prompt.replace("{{json}}", json)

        val request = ChatRequest(
            //model = "gpt-4o",
            // model = "gpt-4-turbo-2024-04-09",
            model = "gpt-4.1",
            messages = listOf(
                Message(
                    role = "user",
                    content = promptWithJson,
                ),
            ),
            response_format = ResponseFormat(type = "json_object"),
        )
        Log.i("OpenAI", "Request start")
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
                        _json.value = response.body()?.choices?.get(0)?.message?.content ?: ""
                    } else {
                        Log.e("OpenAI", "Response failed: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    println("Failed to get chat completion: ${t.message}")
                }
            },
        )
    }

}