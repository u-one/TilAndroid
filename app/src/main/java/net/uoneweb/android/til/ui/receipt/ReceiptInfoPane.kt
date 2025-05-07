package net.uoneweb.android.til.ui.receipt

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import net.uoneweb.android.til.ui.location.Location
import net.uoneweb.android.til.ui.receipt.data.Receipt
import net.uoneweb.android.til.ui.receipt.data.ReceiptMetaData

@Composable
fun ReceiptInfoPane(
    receipt: ReceiptMetaData, imageUri: Uri?, location: Location?,
    onSaveMetaData: (ReceiptMetaData) -> Unit = {},
) {
    if (receipt == ReceiptMetaData.Empty) {
        return
    }

    // TODO: refactor
    val metadata = if (receipt.id == null || receipt.location != location) {
        ReceiptMetaData(receipt.content, receipt.id, location, imageUri?.lastPathSegment)
    } else receipt



    Column {
        Row {
            TextShareButton(metadata.content.title(), metadata.json())
            SaveButton(
                enabled = metadata.id == null || receipt.location != location,
                onClick = { onSaveMetaData(metadata) },
            )
        }
        Row {
            Text(text = "店舗")
            Text(text = metadata.content.store())
        }
        Row {
            Text("合計")
            Text(text = metadata.content.total().toString() + "円")
        }
        Row {
            Text("住所")
            Text(text = metadata.content.address())
        }
        Text("ファイル名:")
        Text(text = metadata.content.title())
        Text("json:")
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = androidx.compose.ui.graphics.Color.LightGray),
            text = metadata.json(),
        )
    }
}

@Composable
fun SaveButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
    ) {
        Text("Save")
    }
}

@Composable
@Preview(showBackground = true)
fun SaveButtonPreview() {
    SaveButton(
        enabled = false,
    )
}

@Composable
@Preview(showBackground = true, widthDp = 320)
fun ReceiptInfoPreview() {
    val json = """
            {
              "store": {
                "name": "◯◯◯",
                "branch": "△△店",
                "tel": "012-3456-7890",
                "address": "東京都千代田区1-2-3",
                "postalCode": "123-4567",
                "website": "https://example.com",
                "email": "test@example.com"
              },
              "receipt": {
                "date": "2025-01-01",
                "time": "12:34"
              },
              "total": 5678 
            }
        """.trimIndent()
    ReceiptInfoPane(ReceiptMetaData(Receipt(json)), null, null)
}

@Composable
@Preview(showBackground = true, widthDp = 320)
fun EmptyReceiptInfoPreview() {
    ReceiptInfoPane(ReceiptMetaData.Empty, null, null)
}