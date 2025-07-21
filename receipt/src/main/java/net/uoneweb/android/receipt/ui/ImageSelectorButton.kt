package net.uoneweb.android.receipt.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import net.uoneweb.android.receipt.R

@Composable
fun ImageSelector(selectedImageUri: Uri?, onImageSelected: (Uri?) -> Unit, modifier: Modifier = Modifier, onImageClick: (() -> Unit)? = null) {
    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        onImageSelected(uri)
    }
    Column(modifier = modifier) {
        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text("Select Image")
        }
        selectedImageUri?.let { uri ->
            val painter = if (LocalInspectionMode.current) {
                painterResource(id = R.drawable.receipt_sample)
            } else {
                rememberAsyncImagePainter(uri)
            }
            Text(uri.toString())
            Image(
                painter = painter,
                contentDescription = "Selected image",
                modifier = Modifier
                    .padding(16.dp)
                    .size(256.dp)
                    .then(if (onImageClick != null) Modifier.clickable { onImageClick() } else Modifier),
            )
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 320)
fun ImageSelectorPreview() {
    val selectedImageUri = "android.resource://sample/drawable/dummy_receipt".toUri()
    ImageSelector(selectedImageUri, onImageSelected = {})
}
