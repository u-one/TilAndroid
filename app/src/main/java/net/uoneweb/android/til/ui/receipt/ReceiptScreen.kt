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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import net.uoneweb.android.til.ui.receipt.Receipt
import net.uoneweb.android.til.ui.receipt.ReceiptViewModel

@Composable
fun ReceiptScreen(viewModel: ReceiptViewModel = viewModel()) {
    val selectedImageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val uploadedImageUri by viewModel.uploadedFileUrl
    val receipt by viewModel.receipt

    //OpenAiUi()
    //AiResult()
    LaunchedEffect(selectedImageUri.value) {
        println("selectedImageUri: $selectedImageUri")
        if (selectedImageUri.value != null) {
            viewModel.generateJsonFromImage(selectedImageUri.value!!)
        }
    }

    ReceiptScreenMain(
        selectedImageUri.value, uploadedImageUri, receipt,
        onImageSelected = { uri ->
            selectedImageUri.value = uri
            viewModel.reset()
        },
        onClickImageUpload = {
            selectedImageUri.value?.let {
                viewModel.uploadImage(it)
            }
        },
    )

}

@Composable
fun ReceiptScreenMain(
    selectedImageUri: Uri?, uploadedImageUri: Uri?, receipt: Receipt,
    onImageSelected: (Uri?) -> Unit,
    onClickImageUpload: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(modifier = Modifier.verticalScroll(scrollState)) {

        ReceiptImageSelector(selectedImageUri) { onImageSelected(it) }

        ReceiptImageUploader(selectedImageUri, uploadedImageUri) { onClickImageUpload() }

        ReceiptInfo(receipt)
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun ReceiptScreenMainProgressPreview() {
    val context = LocalContext.current
    val selectedImageUri = Uri.parse("android.resource://${context.packageName}/drawable/dummy_receipt")
    val uploadedImageUri = Uri.Builder().build()
    val receipt = Receipt.Empty

    ReceiptScreenMain(
        selectedImageUri,
        uploadedImageUri,
        receipt,
        onImageSelected = {},
        onClickImageUpload = {},
    )
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun ReceiptScreenMainPreview() {
    val context = LocalContext.current
    val selectedImageUri = Uri.parse("android.resource://${context.packageName}/drawable/dummy_receipt")
    val uploadedImageUri = Uri.Builder().authority("example.com").build()
    val json = """
            {
              "store": {
                "name": "store",
                "branch": "branch"
              },
              "receipt": {
                "date": "2025-01-01",
                "time": "12:34"
              },
              "total": 5678 
            }
        """.trimIndent()
    val receipt = Receipt(json)

    ReceiptScreenMain(
        selectedImageUri,
        uploadedImageUri,
        receipt,
        onImageSelected = {},
        onClickImageUpload = {},
    )
}

@Composable
fun ReceiptImageSelector(selectedImageUri: Uri?, onImageSelected: (Uri?) -> Unit) {
    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        onImageSelected(uri)
    }
    Column {
        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Select Image")
        }
        selectedImageUri?.let { uri ->
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Selected image",
                modifier = Modifier.size(256.dp),
            )
        }
    }
}

@Composable
fun ReceiptImageUploader(selectedImageUri: Uri?, uploadedImageUri: Uri?, onClickImageUpload: () -> Unit) {
    if (selectedImageUri == null) return
    Column {
        Button(
            onClick = {
                onClickImageUpload()
            },
            enabled = uploadedImageUri == null,
        ) {
            if (uploadedImageUri != null) {
                Text("Uploaded")
            } else {
                Text("Upload Image")
            }
        }

        if (uploadedImageUri != null) {
            Text("Image url: $uploadedImageUri")
        }

    }
}

@Composable
fun ReceiptInfo(receipt: Receipt) {
    if (receipt == Receipt.Empty) {
        return
    }
    Column {
        ShareButton(receipt)
        Text(text = receipt.store())
        Text(text = receipt.total().toString() + "円")
        Text(text = receipt.title())
        Text(text = receipt.json)
    }
}

@Composable
fun ShareButton(receipt: Receipt) {
    val context = LocalContext.current
    if (receipt == Receipt.Empty) {
        return
    }
    Button(
        onClick = {
            val title = receipt.title()
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, receipt.json)
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

@Composable
@Preview(showBackground = true, widthDp = 320)
fun ReceiptImageSelectorPreview() {
    ReceiptImageSelector(selectedImageUri = null) {}
}

@Composable
@Preview(showBackground = true, widthDp = 320)
fun ReceiptImageUploaderPreview() {
    ReceiptImageUploader(selectedImageUri = Uri.Builder().build(), uploadedImageUri = null) {}
}

@Composable
@Preview(showBackground = true, widthDp = 320)
fun ReceiptInfoPreview() {
    val json = """
            {
              "store": {
                "name": "store",
                "branch": "branch"
              },
              "receipt": {
                "date": "2025-01-01",
                "time": "12:34"
              },
              "total": 5678 
            }
        """.trimIndent()
    ReceiptInfo(Receipt(json))
}

@Composable
@Preview(showBackground = true, widthDp = 320)
fun EmptyReceiptInfoPreview() {
    ReceiptInfo(Receipt.Empty)
}