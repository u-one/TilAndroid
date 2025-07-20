package net.uoneweb.android.receipt.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ImagePickerDialog(
    state: ImagePickerDialogState,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("レシート画像の追加") },
        text = { Text("画像を選択またはカメラで撮影してください") },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                    state.onSelectFromGalleryClick()
                },
            ) { Text("画像を選択") }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                    state.onTakePictureClick()
                },
            ) { Text("カメラで撮影") }
        },
    )
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun ImagePickerDialogPreview() {
    val state = rememberImagePickerDialogState(onImageSelected = {})
    ImagePickerDialog(state = state, onDismissRequest = { })
}