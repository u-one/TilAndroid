package net.uoneweb.android.receipt.ui.detail

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import net.uoneweb.android.gis.ui.location.CurrentLocationComponent
import net.uoneweb.android.gis.ui.location.Location
import net.uoneweb.android.receipt.R
import net.uoneweb.android.receipt.data.ReceiptMetaData
import net.uoneweb.android.receipt.ui.ImageSelector
import net.uoneweb.android.receipt.ui.ImageUploaderButton
import net.uoneweb.android.receipt.ui.MapLocationFinder

sealed class ReceiptDetailEvent {
    data class OnImageSelected(val uri: Uri) : ReceiptDetailEvent()
    data class OnLocationSet(val location: Location) : ReceiptDetailEvent()
    data class OnSaveMetaData(val metaData: ReceiptMetaData) : ReceiptDetailEvent()
    object OnClickImageUpload : ReceiptDetailEvent()
    object OnClickTest : ReceiptDetailEvent()
    data class OnCorrectionSubmitted(val correction: String) : ReceiptDetailEvent()
    data class OnCorrectionTextChanged(val text: String) : ReceiptDetailEvent()
}

@Composable
fun ReceiptDetail(uiState: ReceiptDetailUiState, onEvent: (ReceiptDetailEvent) -> Unit) {
    val context = LocalContext.current
    var showPreviewDialog by remember { mutableStateOf(false) }

    if (showPreviewDialog) {
        uiState.selectedImageUri?.let {
            ImagePreviewDialog(uri = it, onDismiss = { showPreviewDialog = false })
        }
    }

    Column {
        Row {
            ImageSelector(
                uiState.selectedImageUri,
                onImageSelected = { it?.let { onEvent(ReceiptDetailEvent.OnImageSelected(it)) } },
                onImageClick = { showPreviewDialog = true }
            )
        }
        Button(onClick = { onEvent(ReceiptDetailEvent.OnClickTest) }) { Text(text = "ReceiptResultTest") }
        ImageUploaderButton(uiState.selectedImageUri, uiState.uploadedImageUri) { onEvent(ReceiptDetailEvent.OnClickImageUpload) }
        HorizontalDivider()
        CurrentLocationComponent(location = uiState.location) { it?.let { onEvent(ReceiptDetailEvent.OnLocationSet(it)) } }
        if (uiState.imageLocation != Location.Empty) {
            Row {
                Button(
                    onClick = {
                        val location = uiState.imageLocation
                        onEvent(ReceiptDetailEvent.OnLocationSet(location))
                    },
                ) {
                    Text(context.getString(R.string.use_image_location))
                }
                Text("${uiState.imageLocation.latitude}, ${uiState.imageLocation.longitude}")
            }
        }
        MapLocationFinder(
            uiState.location ?: Location.Default,
            onLocationSelected = { onEvent(ReceiptDetailEvent.OnLocationSet(it)) },
        )
        HorizontalDivider()

        if (uiState.loading) {
            Text("Analyzing receipt info ...")
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        ReceiptInfoPane(uiState, { onEvent(ReceiptDetailEvent.OnSaveMetaData(it)) })

        OutlinedTextField(
            value = uiState.correctionText,
            onValueChange = { onEvent(ReceiptDetailEvent.OnCorrectionTextChanged(it)) },
            label = { Text("修正内容") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { onEvent(ReceiptDetailEvent.OnCorrectionSubmitted(uiState.correctionText)) },
            enabled = uiState.correctionText.isNotBlank()
        ) {
            Text("修正を送信")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReceiptDetailPreview() {
    val context = LocalContext.current
    val uiState = ReceiptDetailUiState(
        selectedImageUri = "android.resource://${context.packageName}/drawable/dummy_receipt".toUri(),
        uploadedImageUri = Uri.Builder().authority("example.com").build(),
        loading = false,
        location = Location.Default,
    )
    ReceiptDetail(uiState, {})
}