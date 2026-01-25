package net.uoneweb.android.receipt.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.uoneweb.android.receipt.R
import net.uoneweb.android.receipt.data.ReceiptMetaData

data class ReceiptListState(
    val yearMonthOptions: List<String> = emptyList(),
    val selectedYearMonth: String = "",
    val receipts: List<ReceiptMetaData> = emptyList(),
) {
    companion object {
        const val UNKNOWN_DATE_KEY = "__unknown__"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptList(
    state: ReceiptListState,
    onYearMonthSelected: (String) -> Unit = {},
    onClickItem: (ReceiptMetaData) -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            TextField(
                value = formatYearMonth(state.selectedYearMonth),
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                state.yearMonthOptions.forEach { yearMonth ->
                    DropdownMenuItem(
                        text = { Text(formatYearMonth(yearMonth)) },
                        onClick = {
                            onYearMonthSelected(yearMonth)
                            expanded = false
                        },
                    )
                }
            }
        }

        state.receipts.forEach { item ->
            ReceiptListItem(item, onClickItem)
        }
    }
}

@Composable
private fun formatYearMonth(yearMonth: String): String {
    return if (yearMonth == ReceiptListState.UNKNOWN_DATE_KEY) {
        stringResource(R.string.list_unknown_date)
    } else {
        yearMonth.replace("-", "年") + "月"
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
