package net.uoneweb.android.til.ui.receipt

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.uoneweb.android.til.ui.receipt.data.ReceiptMetaData


@Composable
fun ReceiptList(list: List<ReceiptMetaData> = emptyList(), onClickItem: (item: ReceiptMetaData) -> Unit = {}) {
    list.forEach { item ->
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

@Preview(showBackground = true)
@Composable
fun ReceiptListPreview() {
    ReceiptList()
}