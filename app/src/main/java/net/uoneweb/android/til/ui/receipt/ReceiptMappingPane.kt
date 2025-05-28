package net.uoneweb.android.til.ui.receipt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.outlinedCardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.uoneweb.android.til.ui.receipt.data.ReceiptMappingInfo
import net.uoneweb.android.til.ui.receipt.webapi.SampleData

@Composable
fun ReceiptMappingPane(text: String) {
    Column {
        JsonContentViewer(text)
        Text("AI Response:")
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.LightGray),
            text = text,
        )
    }
}

@Composable
fun JsonContentViewer(jsonString: String) {
    val receiptMappingInfo = ReceiptMappingInfo.fromJson(jsonString)
    if (receiptMappingInfo == ReceiptMappingInfo.Empty) {
        return
    }

    ReceiptMapInfoViewer(receiptMappingInfo)
}

@Composable
fun ReceiptMapInfoViewer(receiptMappingInfo: ReceiptMappingInfo) {
    Surface(color = MaterialTheme.colorScheme.primaryContainer) {
        Column(modifier = Modifier.padding(16.dp)) {
            ExtractedItemsCard(receiptMappingInfo)

            Spacer(modifier = Modifier.height(16.dp))

            ActualItemsCard(receiptMappingInfo)

            Spacer(modifier = Modifier.height(16.dp))

            ComparingItemsCard(receiptMappingInfo)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Update Recommendation")
            Card(modifier = Modifier.padding(2.dp)) {
                Text(receiptMappingInfo.updateRecommendation, modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun ExtractedItemsCard(receiptMappingInfo: ReceiptMappingInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        colors = outlinedCardColors(),
    ) {
        Text("Store Info Extracted")
        Column {
            receiptMappingInfo.storeInfoExtracted.forEach { item ->
                ExtractedTagItem(item)
            }
        }
    }
}

@Composable
fun ActualItemsCard(receiptMappingInfo: ReceiptMappingInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        colors = outlinedCardColors(),
    ) {
        Text("Existing Store Info")
        Column {
            receiptMappingInfo.existingStoreInfo.tags.forEach { tag ->
                ExistingStoreTagItem(tag)
            }
        }
    }
}

@Composable
fun ComparingItemsCard(receiptMappingInfo: ReceiptMappingInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        colors = outlinedCardColors(),
    ) {
        Text("Comparison")
        Column {
            receiptMappingInfo.comparison.forEach { comparison ->
                ComparingItem(comparison)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 600, heightDp = 1200)
@Composable
fun PreviewReceiptMappingPane() {
    val json = SampleData.shortSample()

    ReceiptMappingPane(json)
}