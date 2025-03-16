import android.net.Uri
import android.util.Base64
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
import net.uoneweb.android.til.ui.receipt.ChatRequest
import net.uoneweb.android.til.ui.receipt.ChatResponse
import net.uoneweb.android.til.ui.receipt.Message
import net.uoneweb.android.til.ui.receipt.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream

@Composable
fun ReceiptScreen() {
    val context = LocalContext.current
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val responseText = remember { mutableStateOf("No response yet") }

    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri.value = uri
    }

    fun encodeImageToBase64(uri: Uri): String? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()
            Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun makeApiRequest(data: String) {
        val request = ChatRequest(
            model = "gpt-4",
            messages = listOf(
                Message(role = "system", content = "You are a helpful assistant."),
                Message(
                    role = "user",
                    content = "[{\"type\":\"text\", \"text\":\"画像の内容をレシートの情報を表現するjsonにしてください\"}" +
                            "{\"type\":\"image_url\", \"image_url\":\"{\"url\":\"data:image/jpeg;base64," + data + "\"}\"}]",
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
                encodeImageToBase64(selectedImageUri.value!!)?.let {
                    //TODO: APIエラー。画像のサイズを小さくする必要がありそう
                    makeApiRequest(it)
                }
            },
        ) {
            Text("Submit")
        }
        Text(text = responseText.value)
    }

}
