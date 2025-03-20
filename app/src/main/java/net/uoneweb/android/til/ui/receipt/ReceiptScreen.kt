import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.Firebase
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.vertexAI
import net.uoneweb.android.til.ui.receipt.GeminiReceiptPrompt
import net.uoneweb.android.til.ui.receipt.GeminiReceiptResponse
import net.uoneweb.android.til.ui.receipt.ReceiptViewModel

@Composable
fun ReceiptScreen(viewModel: ReceiptViewModel = viewModel()) {
    val context = LocalContext.current
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    //OpenAiUi()
    //AiResult()

    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri.value = uri
    }

    LaunchedEffect(Unit) {
    }

    val uploaded = remember { mutableStateOf(false) }
    val uploadedImageUrl by viewModel.uploadedFileUrl

    val scrollState = rememberScrollState()

    Column(modifier = Modifier.verticalScroll(scrollState)) {

        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Select Image")
        }
        selectedImageUri.value?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Selected image",
                modifier = Modifier.size(256.dp),
            )

            Button(
                onClick = {
                    viewModel.uploadImage(uri)
                },
                enabled = !uploaded.value,
            ) {
                if (uploaded.value) {
                    Text("Uploaded")
                } else {
                    Text("Upload Image")
                }
            }

            Text("Image url: ${uploadedImageUrl?.toString() ?: ""}")
        }

        AiResult(imageUrl = selectedImageUri.value)
    }
}


@Composable
fun AiResult(imageUrl: Uri?) {
    if (imageUrl == null) {
        return
    }
    val text = remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val model = Firebase.vertexAI.generativeModel("gemini-2.0-flash")
        // selectedImageUri から bitmap を取得する
        val bitmap: Bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(imageUrl))
        val prompt = content {
            image(bitmap)
            text(GeminiReceiptPrompt.text)
        }
        val response = model.generateContent(prompt)
        print(response.text)
        val parser = GeminiReceiptResponse(response.text)
        print(parser.json())
        text.value = parser.json()
    }

    Text(text = text.value ?: "Loading...")
}

