package net.uoneweb.android.til.ui.receipt

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun OpenAiUi() {
    val context = LocalContext.current
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val fileId = remember { mutableStateOf<String?>(null) }
    val responseText = remember { mutableStateOf("No response yet") }

    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri.value = uri
    }

    fun makeApiRequest(fileId: String) {
        val request = ChatRequest(
            model = "gpt-4",
            messages = listOf(
                Message(
                    role = "user",
                    content = "画像の内容をレシートの情報を表現するjsonにしてください",
                ),
            ),
        )

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
                        responseText.value = "Response successful"
                    } else {
                        println("${response.errorBody()?.string()}")
                        responseText.value = "Response failed: ${response.errorBody()?.string()}"
                    }
                }

                override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                    println("Failed to get chat completion: ${t.message}")
                }
            },
        )
    }

    fun uploadImage(imageUri: Uri, callback: (String?) -> Unit) {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val imagePart = inputStream?.readBytes()?.let {
            MultipartBody.Part.createFormData("file", "receipt.jpg", it.toRequestBody("image/*".toMediaTypeOrNull()))
        }
        val purpose = "assistants".toRequestBody("text/plain".toMediaTypeOrNull())
        RetrofitInstance.api.uploadFile(imagePart!!, purpose).enqueue(
            object : Callback<FileUploadResponse> {
                override fun onResponse(call: Call<FileUploadResponse>, response: Response<FileUploadResponse>) {
                    if (response.isSuccessful) {
                        val fileUploadResponse = response.body()
                        println(fileUploadResponse)
                        callback(fileUploadResponse?.id)
                    } else {
                        println("${response.errorBody()?.string()}")
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                    println("Failed to upload file: ${t.message}")
                    callback(null)
                }
            },
        )

    }

    Column {
        Text("Receipt")

        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Select Image")
        }

        selectedImageUri.value?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Selected image",
                modifier = Modifier.size(256.dp),
            )
        }

        Button(
            onClick = {
                selectedImageUri.value?.let { uri ->
                    uploadImage(uri) {
                        fileId.value = it
                    }
                }
            },
        ) {
            Text("Upload Image")
        }

        Button(
            onClick = {
                //TODO: APIエラー。画像のサイズを小さくする必要がありそう
                fileId.value?.let {
                    makeApiRequest(it)
                }
            },
        ) {
            Text("Submit")
        }
        Text(text = responseText.value)
    }

}