package net.uoneweb.android.til.receiptmapping.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.uoneweb.android.til.ui.receipt.data.ReceiptMappingInfo


@Composable
fun ReceiptMappingList(receiptMappingInfoList: List<ReceiptMappingInfo> = emptyList(), onClickItem: (ReceiptMappingInfo) -> Unit = {}) {
    receiptMappingInfoList.forEach { item ->
        Row(
            modifier = Modifier
                .padding(8.dp)
                .clickable { onClickItem(item) },
        ) {
            Text(item.id.toString() + " " + item.receiptId.toString() + " " + item.updateRecommendation.substring(0..20), modifier = Modifier.padding(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReceiptMappingListPreview() {
    ReceiptMappingList()
}