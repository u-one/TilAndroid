package net.uoneweb.android.receipt.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider.getUriForFile
import java.io.File.createTempFile

@Composable
fun ImagePickerDialog(onImageSelected: (Uri) -> Unit, onDismissRequest: () -> Unit) {
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { onImageSelected(uri) }
    }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success: Boolean ->
        if (success) {
            cameraImageUri?.let { onImageSelected(it) }
            cameraImageUri = null
        }
    }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("レシート画像の追加") },
        text = { Text("画像を選択またはカメラで撮影してください") },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                    imagePickerLauncher.launch("image/*")
                },
            ) { Text("画像を選択") }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                    // 一時ファイルUriを生成
                    val uri = getUriForFile(
                        context,
                        context.packageName + ".fileprovider",
                        createTempFile("receipt_", ".jpg", context.cacheDir),
                    )
                    cameraImageUri = uri
                    cameraLauncher.launch(uri)
                },
            ) { Text("カメラで撮影") }
        },
    )
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun ImagePickerDialogPreview() {
    ImagePickerDialog({}, { })
}