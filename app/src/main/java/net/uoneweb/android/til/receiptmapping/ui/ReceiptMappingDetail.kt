package net.uoneweb.android.til.receiptmapping.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import net.uoneweb.android.gis.ui.location.Location
import net.uoneweb.android.gis.ui.map.Feature
import net.uoneweb.android.gis.ui.map.MapFeatureFinderDialog
import net.uoneweb.android.til.ui.receipt.ReceiptMappingPane
import net.uoneweb.android.til.ui.receipt.webapi.SampleData

data class ReceiptMappingUiState(
    val isLoading: Boolean = false,
    val receiptMappingInfo: String = "",
    val location: Location = Location.Default,
)

sealed class ReceiptMappingEvent {
    data class OnInferClicked(val feature: Feature? = Feature.Empty) : ReceiptMappingEvent()
    object OnTestInferClicked : ReceiptMappingEvent()
}

@Composable
fun ReceiptMappingDetail(
    uiState: ReceiptMappingUiState,
    onEvent: (ReceiptMappingEvent) -> Unit,
) {

    var expandFeatureFinderDialog by remember { mutableStateOf(false) }

    Column {
        Button(onClick = { expandFeatureFinderDialog = true }) {
            Text("Select Feature from Map")
        }
        Row {
            Button(onClick = { onEvent(ReceiptMappingEvent.OnInferClicked()) }) {
                Text("ReceiptMappingInfo")
            }
            Button(onClick = { onEvent(ReceiptMappingEvent.OnTestInferClicked) }) {
                Text("TestReceiptMappingInfo")
            }
        }
        ReceiptMappingPane(uiState.receiptMappingInfo)
        if (uiState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        if (expandFeatureFinderDialog) {
            MapFeatureFinderDialog(
                location = uiState.location,
                onFeatureSelected = { feature ->
                    println("Selected feature: $feature")
                    expandFeatureFinderDialog = false
                    onEvent(ReceiptMappingEvent.OnInferClicked(feature))

                },
                onDismissRequest = { expandFeatureFinderDialog = false },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReceiptMappingDetailPreview() {
    val receiptMappingInfo = SampleData.shortSample()
    val uiState = ReceiptMappingUiState(
        isLoading = false,
        receiptMappingInfo = receiptMappingInfo,
        location = Location.Default,
    )
    ReceiptMappingDetail(uiState, {})
}