package net.uoneweb.android.receipt.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

@Composable
fun rememberImagePickerDialogState(
    onImageSelected: (Uri) -> Unit,
): ImagePickerDialogState {
    val context = LocalContext.current
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let(onImageSelected)
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                cameraImageUri?.let(onImageSelected)
            }
        }

    return remember {
        ImagePickerDialogState(
            onSelectFromGalleryClick = {
                imagePickerLauncher.launch("image/*")
            },
            onTakePictureClick = {
                val uri = FileProvider.getUriForFile(
                    context,
                    context.packageName + ".fileprovider",
                    File.createTempFile("receipt_", ".jpg", context.cacheDir)
                )
                cameraImageUri = uri
                cameraLauncher.launch(uri)
            }
        )
    }
}

class ImagePickerDialogState(
    val onSelectFromGalleryClick: () -> Unit,
    val onTakePictureClick: () -> Unit,
)
