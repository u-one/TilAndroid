package net.uoneweb.android.receipt.ui.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import net.uoneweb.android.receipt.R
import net.uoneweb.android.receipt.UNKNOWN_DATE_KEY

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearMonthDropdown(
    options: List<String>,
    selectedYearMonth: String,
    onYearMonthSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier,
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
            options.forEach { yearMonth ->
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
}

@Composable
private fun formatYearMonth(yearMonth: String): String {
    return if (yearMonth == UNKNOWN_DATE_KEY) {
        stringResource(R.string.list_unknown_date)
    } else {
        yearMonth.replace("-", "年") + "月"
    }
}

@Preview(showBackground = true)
@Composable
fun YearMonthDropdownPreview() {
    YearMonthDropdown(
        options = listOf("2025-01", "2025-02", UNKNOWN_DATE_KEY),
        selectedYearMonth = "2025-01",
        onYearMonthSelected = {},
    )
}
