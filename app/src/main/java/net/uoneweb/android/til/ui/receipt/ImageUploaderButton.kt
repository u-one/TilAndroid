package net.uoneweb.android.til.ui.receipt

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri

@Composable
fun ImageUploaderButton(selectedImageUri: Uri?, uploadedImageUri: Uri?, onClickImageUpload: () -> Unit) {
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
@Preview(showBackground = true, widthDp = 320)
fun ImageUploaderButtonPreview() {
    val selectedImageUri = "android.resource://sample/drawable/dummy_receipt".toUri()
    val uploadedImageUri = "https://example.com/uploaded_image.jpg".toUri()
    ImageUploaderButton(selectedImageUri, uploadedImageUri) {}
}
