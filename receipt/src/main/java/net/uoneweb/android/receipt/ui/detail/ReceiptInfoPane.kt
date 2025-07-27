package net.uoneweb.android.receipt.ui.detail

import InfoRow
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import net.uoneweb.android.gis.ui.location.Location
import net.uoneweb.android.receipt.data.Item
import net.uoneweb.android.receipt.data.Receipt
import net.uoneweb.android.receipt.data.ReceiptMetaData
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ReceiptInfoPane(
    receiptDetailUiState: ReceiptDetailUiState,
) {
    val metadata = receiptDetailUiState.receipt
    if (metadata == ReceiptMetaData.Empty) {
        return
    }

    var showFullJson by remember { mutableStateOf(false) }
    var showItems by remember { mutableStateOf(true) }
    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale.JAPAN) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp), // Adjust padding for new card structure
    ) {
        Text(
            text = "レシート情報",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        // 店舗情報
        InfoRow(
            icon = Icons.Default.Store,
            label = "店舗",
            value = metadata.content.store(),
            color = MaterialTheme.colorScheme.primary,
        )

        // 日付・時間情報
        if (metadata.content.receipt.date.isNotEmpty() || metadata.content.receipt.time.isNotEmpty()) {
            val dateTime = buildString {
                append(metadata.content.receipt.date)
                if (metadata.content.receipt.date.isNotEmpty() && metadata.content.receipt.time.isNotEmpty()) {
                    append(" ")
                }
                append(metadata.content.receipt.time)
            }

            InfoRow(
                icon = Icons.Default.CalendarToday,
                label = "日時",
                value = dateTime,
                color = MaterialTheme.colorScheme.secondary,
            )
        }

        // 住所情報
        if (metadata.content.address().isNotEmpty()) {
            InfoRow(
                icon = Icons.Default.LocationOn,
                label = "住所",
                value = metadata.content.address(),
                color = MaterialTheme.colorScheme.error,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        // 商品項目セクション
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 8.dp),
                )
                Text(
                    text = "商品項目 (${metadata.content.items.size}点)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            Button(
                onClick = { showItems = !showItems },
                colors = ButtonDefaults.textButtonColors(),
            ) {
                Text(if (showItems) "折りたたむ" else "展開する")
            }
        }

        if (showItems && metadata.content.items.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            // 商品リストのヘッダー
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 8.dp),
            ) {
                Text(
                    text = "商品名",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(3f),
                )
                Text(
                    text = "数量",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = "金額",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(2f),
                )
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))

            // 商品リスト
            metadata.content.items.forEach { item ->
                ItemRow(item, currencyFormatter)
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))

            // 合計金額
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.weight(3f))
                Text(
                    text = "合計",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = currencyFormatter.format(metadata.content.total()),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(2f),
                )
            }
        } else if (metadata.content.items.isEmpty()) {
            Text(
                text = "商品情報がありません",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        // ファイル名
        Text(
            text = "ファイル名:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = metadata.content.title(),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 8.dp, top = 4.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // JSON表示トグルボタン
        Button(
            onClick = { showFullJson = !showFullJson },
            modifier = Modifier.align(Alignment.End),
        ) {
            Text(if (showFullJson) "JSONを隠す" else "JSONを表示")
        }

        // JSON表示エリア
        if (showFullJson) {
            Spacer(modifier = Modifier.height(8.dp))
            JsonTextView(metadata)
        }
    }
}

@Composable
fun ItemRow(item: Item, currencyFormatter: NumberFormat) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(3f),
        )
        Text(
            text = if ((item.quantity ?: 0f) > 0f) item.quantity.toString() else "1",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
        )
        if (item.price != null) {
            Text(
                text = currencyFormatter.format(item.price),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(2f),
            )
        }
    }
}

@Composable
private fun JsonTextView(metadata: ReceiptMetaData) {
    Column {
        Text(
            text = "JSON データ:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            ) {
                Text(
                    text = metadata.json(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 320)
fun ReceiptInfoPreview() {

    val context = LocalContext.current
    val uiState = ReceiptDetailUiState(
        selectedImageUri = "android.resource://${context.packageName}/drawable/dummy_receipt".toUri(),
        uploadedImageUri = Uri.Builder().authority("example.com").build(),
        receipt = ReceiptMetaData(Receipt.Sample2),
        loading = false,
        currentLocation = Location.Default,
    )
    MaterialTheme {
        ReceiptInfoPane(uiState)
    }
}

@Composable
@Preview(showBackground = true, widthDp = 320)
fun EmptyReceiptInfoPreview() {
    val uiState = ReceiptDetailUiState(
        receipt = ReceiptMetaData.Empty,
        loading = false,
        currentLocation = Location.Default,
    )
    MaterialTheme {
        ReceiptInfoPane(uiState)
    }
}
