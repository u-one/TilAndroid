package net.uoneweb.android.til.ui.receipt

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter

@Composable
fun ImageSelector(selectedImageUri: Uri?, onImageSelected: (Uri?) -> Unit) {
    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        onImageSelected(uri)
    }
    Column {
        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Select Image")
        }
        selectedImageUri?.let { uri ->
            Text(uri.toString())
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Selected image",
                modifier = Modifier.size(256.dp),
            )
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 320)
fun ImageSelectorPreview() {
    val selectedImageUri = "android.resource://sample/drawable/dummy_receipt".toUri()
    ImageSelector(selectedImageUri) {}
}
