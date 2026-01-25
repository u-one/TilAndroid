package net.uoneweb.android.receipt.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import net.uoneweb.android.receipt.R
import net.uoneweb.android.receipt.data.ReceiptMetaData
import net.uoneweb.android.receipt.ui.ReceiptViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptList(
    list: List<ReceiptMetaData> = emptyList(),
    onClickItem: (item: ReceiptMetaData) -> Unit = {},
    viewModel: ReceiptViewModel = viewModel(),
) {
    val yearMonthList by viewModel.yearMonthList.collectAsState()
    val unknownDateCount by viewModel.unknownDateCount.collectAsState()
    val selectedYearMonth by viewModel.selectedYearMonth.collectAsState()

    val allOptions = buildList {
        addAll(yearMonthList)
        if (unknownDateCount > 0) {
            add(ReceiptViewModel.UNKNOWN_DATE_KEY)
        }
    }

    val receipts by viewModel.getReceiptsForMonth(selectedYearMonth).collectAsState()

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
                value = formatYearMonth(selectedYearMonth),
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
                allOptions.forEach { yearMonth ->
                    DropdownMenuItem(
                        text = { Text(formatYearMonth(yearMonth)) },
                        onClick = {
                            viewModel.selectYearMonth(yearMonth)
                            expanded = false
                        },
                    )
                }
            }
        }

        receipts.forEach { item ->
            ReceiptListItem(item, onClickItem)
        }
    }
}

@Composable
private fun formatYearMonth(yearMonth: String): String {
    return if (yearMonth == ReceiptViewModel.UNKNOWN_DATE_KEY) {
        stringResource(R.string.list_unknown_date)
    } else {
        yearMonth.replace("-", "年") + "月"
    }
}

@Composable
fun ReceiptListItem(
    item: ReceiptMetaData = ReceiptMetaData.Empty,
    onClickItem: (item: ReceiptMetaData) -> Unit = {},
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
fun ReceiptListItemPreview() {
    ReceiptListItem(ReceiptMetaData.Sample)
}
