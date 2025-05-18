package net.uoneweb.android.gis.ui.map

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun StyleSelectorDialog(
    expanded: Boolean = false,
    currentStyle: Style? = null,
    onDismissRequest: () -> Unit = {},
    onStyleSelected: (Style) -> Unit = {},
) {
    if (!expanded) {
        return
    }
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                items(Styles.styles) { style ->
                    DialogItem(
                        style, style != currentStyle,
                        {
                            onDismissRequest()
                            onStyleSelected(it)
                        },
                    )
                }
            }
            Text(
                text = "This is a minimal dialog",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}


@Composable
fun DialogItem(item: Style, enabled: Boolean = true, onClick: (Style) -> Unit = {}) {
    val modifier = if (enabled) {
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable() {
                onClick(item)
            }
    } else {
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceDim)
            .padding(8.dp)
    }

    Row(
        modifier = modifier,
    ) {
        Text(
            item.name,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            item.org,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.Bottom),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StyleSelectorDialogPreview() {
    StyleSelectorDialog(true, currentStyle = Style.Rekichizu)
}