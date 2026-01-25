package net.uoneweb.android.receipt.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.uoneweb.android.receipt.data.ReceiptMetaData

data class ReceiptListState(
    val yearMonthOptions: List<String> = emptyList(),
    val selectedYearMonth: String = "",
    val receipts: List<ReceiptMetaData> = emptyList(),
)

@Composable
fun ReceiptList(
    state: ReceiptListState,
    onYearMonthSelected: (String) -> Unit = {},
    onClickItem: (ReceiptMetaData) -> Unit = {},
) {
    Column {
        YearMonthDropdown(
            options = state.yearMonthOptions,
            selectedYearMonth = state.selectedYearMonth,
            onYearMonthSelected = onYearMonthSelected,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        )

        state.receipts.forEach { item ->
            ReceiptListItem(item, onClickItem)
        }
    }
}

@Composable
fun ReceiptListItem(
    item: ReceiptMetaData = ReceiptMetaData.Empty,
    onClickItem: (ReceiptMetaData) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .clickable { onClickItem(item) },
    ) {
        Text(item.content.title(), style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true)
@Composable
fun ReceiptListPreview() {
    ReceiptList(
        state = ReceiptListState(
            yearMonthOptions = listOf("2025-01", "2025-02"),
            selectedYearMonth = "2025-01",
            receipts = listOf(ReceiptMetaData.Sample),
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun ReceiptListItemPreview() {
    ReceiptListItem(ReceiptMetaData.Sample)
}
