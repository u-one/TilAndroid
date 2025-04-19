import android.content.Intent
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import net.uoneweb.android.til.ui.receipt.Receipt
import net.uoneweb.android.til.ui.receipt.ReceiptViewModel

@Composable
fun ReceiptScreen(viewModel: ReceiptViewModel = viewModel()) {
    val selectedImageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri.value = uri
        viewModel.reset()
    }

    val uploadedImageUrl by viewModel.uploadedFileUrl
    val receiptJson by viewModel.json

    //OpenAiUi()
    //AiResult()

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
                enabled = uploadedImageUrl == null,
            ) {
                if (uploadedImageUrl != null) {
                    Text("Uploaded")
                } else {
                    Text("Upload Image")
                }
            }

            Text("Image url: ${uploadedImageUrl?.toString() ?: ""}")
        }

        LaunchedEffect(selectedImageUri.value) {
            println("selectedImageUri: $selectedImageUri")
            if (selectedImageUri.value != null) {
                viewModel.generateJsonFromImage(selectedImageUri.value!!)
            }
        }
        ShareButton(receiptJson)
        Text(text = receiptJson)
    }

}

@Composable
fun ShareButton(text: String) {
    val context = LocalContext.current
    if (text.isEmpty()) {
        return
    }
    Button(
        onClick = {
            val title = Receipt(text).title()
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                putExtra(Intent.EXTRA_SUBJECT, title)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        },
    ) {
        Text("Share")
    }
}
