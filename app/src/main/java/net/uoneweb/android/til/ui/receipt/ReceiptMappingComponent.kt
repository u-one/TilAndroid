package net.uoneweb.android.til.ui.receipt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import net.uoneweb.android.til.ui.receipt.data.Certainty
import net.uoneweb.android.til.ui.receipt.data.Difference
import net.uoneweb.android.til.ui.receipt.data.ReceiptMappingInfo
import net.uoneweb.android.til.ui.receipt.webapi.SampleData

@Composable
fun ReceiptMappingComponent(text: String) {
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
    val gson = Gson()
    val receiptMappingInfo = gson.fromJson(jsonString, ReceiptMappingInfo::class.java)
    if (receiptMappingInfo == null) {
        Text("Invalid JSON")
        return
    }
    ReceiptMapInfoViewer(receiptMappingInfo)
}

@Composable
fun ReceiptMapInfoViewer(receiptMappingInfo: ReceiptMappingInfo) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Store Info Extracted
        Text("Store Info Extracted")
        LazyRow {
            items(receiptMappingInfo.storeInfoExtracted) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        CertaintyLabel(item.basedOn)
                        Text(
                            "${item.key}: ${item.value}",
                            modifier = Modifier.padding(bottom = 8.dp),
                        )
                        Text(item.reason)
                        Text(item.comment)

                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Existing Store Info")
        LazyRow {
            items(receiptMappingInfo.existingStoreInfo.tags) { tag ->
                Card(modifier = Modifier.padding(2.dp)) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("${tag.key}: ${tag.value}", modifier = Modifier.padding(bottom = 8.dp))
                        Text(tag.comment)
                    }
                }

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Comparison
        Text("Comparison")
        LazyRow {
            items(receiptMappingInfo.comparison) { comparison ->
                Card(modifier = Modifier.padding(2.dp)) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        DifferenceLabel(difference = comparison.difference)
                        Text("${comparison.key}: ${comparison.existingValue} -> ${comparison.newValue}", modifier = Modifier.padding(bottom = 8.dp))
                        Text(comparison.comment)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Update Recommendation")
        Card(modifier = Modifier.padding(2.dp)) {
            Text(receiptMappingInfo.updateRecommendation, modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun CertaintyLabel(certainty: Certainty) {
    val color = when (certainty) {
        Certainty.Unknown -> Color.Gray
        Certainty.Fact -> Color.Green
        Certainty.Inference -> Color.Yellow
    }
    val text = when (certainty) {
        Certainty.Unknown -> "Unknown"
        Certainty.Fact -> "事実ベース"
        Certainty.Inference -> "予測ベース"
    }
    Box(
        modifier = Modifier
            .background(color = color, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
    ) {
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCertaintyLabel() {
    CertaintyLabel(Certainty.Fact)
}

@Composable
fun DifferenceLabel(difference: Difference) {
    val color = when (difference) {
        Difference.Unknown -> Color.Gray
        Difference.Same -> Color.Gray
        Difference.Different -> Color.Yellow
        Difference.New -> Color.Red

    }
    val text = when (difference) {
        Difference.Unknown -> "Unknown"
        Difference.Same -> "差異なし"
        Difference.Different -> "更新"
        Difference.New -> "新規"
    }
    Box(
        modifier = Modifier
            .background(color = color, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
    ) {
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDifferenceLabel() {
    DifferenceLabel(Difference.Different)
}


@Preview(showBackground = true, widthDp = 600)
@Composable
fun PreviewOpenStreetMapContent() {
    val context = LocalContext.current
    val json = SampleData.responseSample(context)

    ReceiptMappingComponent(json)
}