package net.uoneweb.android.receipt.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.uoneweb.android.receipt.data.ReceiptMetaData


@Composable
fun ReceiptList(list: List<ReceiptMetaData> = emptyList(), onClickItem: (item: ReceiptMetaData) -> Unit = {}) {
    val sortedList = list.sortedWith { lsv, rsv ->
        if (lsv.content.title() < rsv.content.title()) {
            -1
        } else if (lsv.content.title() > rsv.content.title()) {
            1
        } else {
            0
        }

    }
    Column {
        sortedList.forEach { item ->
            ReceiptListItem(item, onClickItem)
        }
    }
}

@Composable
fun ReceiptListItem(item: ReceiptMetaData = ReceiptMetaData.Empty, onClickItem: (item: ReceiptMetaData) -> Unit = {}) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                onClickItem(item)
            },
    ) {
        Text(item.content.title(), style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun ReceiptListPreview() {
    ReceiptList(listOf(ReceiptMetaData.Sample, ReceiptMetaData.Empty))
}

@Preview(showBackground = true)
@Composable
fun ReceiptListItemPreview() {
    ReceiptListItem(ReceiptMetaData.Sample)
}