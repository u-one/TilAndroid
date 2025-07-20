package net.uoneweb.android.receipt.ui.detail

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage

@Composable
fun ImagePreviewDialog(
    uri: Uri,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        AsyncImage(
            model = uri,
            contentDescription = "receipt image",
            modifier = Modifier
                .fillMaxSize()
                .clickable { onDismiss() },
            contentScale = ContentScale.Fit,
        )
    }
}
