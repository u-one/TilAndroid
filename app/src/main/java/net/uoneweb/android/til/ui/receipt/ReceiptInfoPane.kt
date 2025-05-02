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
    receipt: Receipt, imageUri: Uri?, location: Location?,
    onSaveMetaData: (ReceiptMetaData) -> Unit = {},
) {
    if (receipt == Receipt.Empty) {
        return
    }

    val metadata = ReceiptMetaData(receipt, location, imageUri?.lastPathSegment)
    Column {
        Row {
            TextShareButton(receipt.title(), metadata.json())
            Button(
                onClick = { onSaveMetaData(metadata) },
            ) {
                Text("Save")
            }
        }
        Row {
            Text(text = "店舗")
            Text(text = receipt.store())
        }
        Row {
            Text("合計")
            Text(text = receipt.total().toString() + "円")
        }
        Row {
            Text("住所")
            Text(text = receipt.address())
        }
        Text("ファイル名:")
        Text(text = receipt.title())
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
    ReceiptInfoPane(Receipt(json), null, null)
}

@Composable
@Preview(showBackground = true, widthDp = 320)
fun EmptyReceiptInfoPreview() {
    ReceiptInfoPane(Receipt.Empty, null, null)
}