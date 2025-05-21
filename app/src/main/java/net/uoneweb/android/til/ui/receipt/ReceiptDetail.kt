package net.uoneweb.android.til.ui.receipt

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import net.uoneweb.android.gis.ui.location.CurrentLocationComponent
import net.uoneweb.android.gis.ui.location.Location
import net.uoneweb.android.til.ui.receipt.data.ReceiptMetaData

sealed class ReceiptDetailEvent {
    data class OnImageSelected(val uri: Uri) : ReceiptDetailEvent()
    data class OnLocationSet(val location: Location) : ReceiptDetailEvent()
    data class OnSaveMetaData(val metaData: ReceiptMetaData) : ReceiptDetailEvent()
    object OnClickImageUpload : ReceiptDetailEvent()
    object OnClickTest : ReceiptDetailEvent()
}

@Composable
fun ReceiptDetail(uiState: ReceiptDetailUiState, onEvent: (ReceiptDetailEvent) -> Unit) {
    Column {
        Row {
            ImageSelector(uiState.selectedImageUri) { it?.let { onEvent(ReceiptDetailEvent.OnImageSelected(it)) } }
            Button(onClick = { onEvent(ReceiptDetailEvent.OnClickTest) }) { Text(text = "ReceiptResultTest") }
        }
        ImageUploaderButton(uiState.selectedImageUri, uiState.uploadedImageUri) { onEvent(ReceiptDetailEvent.OnClickImageUpload) }
        CurrentLocationComponent(location = uiState.location) { it?.let { onEvent(ReceiptDetailEvent.OnLocationSet(it)) } }
        MapLocationFinder(
            uiState.location ?: Location.Default,
            onLocationSelected = { onEvent(ReceiptDetailEvent.OnLocationSet(it)) },
        )

        if (uiState.loading) {
            Text("Analyzing receipt info ...")
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        ReceiptInfoPane(uiState.receipt, uiState.selectedImageUri, uiState.location, { onEvent(ReceiptDetailEvent.OnSaveMetaData(it)) })
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