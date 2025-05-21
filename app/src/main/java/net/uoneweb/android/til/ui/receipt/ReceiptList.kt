package net.uoneweb.android.til.ui.receipt

import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.uoneweb.android.til.ui.receipt.data.ReceiptMetaData


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
    sortedList.forEach { item ->
        Row(
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    onClickItem(item)
                },
        ) {
            Text(item.content.title())
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
        Text(item.content.title(), style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp))
    }
}

@Preview(showBackground = true)
@Composable
fun ReceiptListPreview() {
    ReceiptList()
}

@Preview(showBackground = true)
@Composable
fun ReceiptListItemPreview() {
    ReceiptListItem()
}