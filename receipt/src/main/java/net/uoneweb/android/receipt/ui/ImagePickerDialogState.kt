package net.uoneweb.android.receipt.ui

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
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
    val state = remember { ImagePickerDialogState(context) }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let(onImageSelected)
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
            if (success) {
                state.cameraImageUri?.let(onImageSelected)
            }
            state.clearCameraImageUri()
        }

    state.imagePickerLauncher = imagePickerLauncher
    state.cameraLauncher = cameraLauncher

    return state
}

class ImagePickerDialogState(
    private val context: Context,
) {
    internal lateinit var imagePickerLauncher: ManagedActivityResultLauncher<String, Uri?>
    internal lateinit var cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>

    internal var cameraImageUri: Uri? by mutableStateOf(null)
        private set

    fun onSelectFromGalleryClick() {
        imagePickerLauncher.launch("image/*")
    }

    fun onTakePictureClick() {
        val uri =
            FileProvider.getUriForFile(
                context,
                context.packageName + ".fileprovider",
                File.createTempFile("receipt_", ".jpg", context.cacheDir),
            )
        cameraImageUri = uri
        cameraLauncher.launch(uri)
    }

    fun clearCameraImageUri() {
        cameraImageUri = null
    }
}
