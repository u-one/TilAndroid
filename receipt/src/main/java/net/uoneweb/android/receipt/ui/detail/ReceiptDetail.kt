package net.uoneweb.android.receipt.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import net.uoneweb.android.gis.ui.location.CurrentLocationComponent
import net.uoneweb.android.gis.ui.location.Location
import net.uoneweb.android.receipt.R
import net.uoneweb.android.receipt.data.ReceiptMetaData
import net.uoneweb.android.receipt.ui.ImageSelector
import net.uoneweb.android.receipt.ui.ImageUploaderButton
import net.uoneweb.android.receipt.ui.MapLocationFinder

sealed class ReceiptDetailEvent {
    data class OnImageSelected(val uri: Uri) : ReceiptDetailEvent()
    data class OnLocationSet(val location: Location) : ReceiptDetailEvent()
    data class OnSaveMetaData(val metaData: ReceiptMetaData) : ReceiptDetailEvent()
    object OnClickImageUpload : ReceiptDetailEvent()
    object OnClickTest : ReceiptDetailEvent()
    data class OnCorrectionSubmitted(val correction: String) : ReceiptDetailEvent()
    data class OnCorrectionTextChanged(val text: String) : ReceiptDetailEvent()
}

@Composable
fun ReceiptDetail(uiState: ReceiptDetailUiState, onEvent: (ReceiptDetailEvent) -> Unit) {
    val context = LocalContext.current
    var showPreviewDialog by remember { mutableStateOf(false) }

    if (showPreviewDialog) {
        uiState.selectedImageUri?.let {
            ImagePreviewDialog(uri = it, onDismiss = { showPreviewDialog = false })
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Text(
            text = "レシート詳細",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
        )


        // 画像選択・アップロードセクション
        Card(
            modifier = Modifier
                .fillMaxWidth()

                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(
                    text = "画像",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                ImageSelector(
                    uiState.selectedImageUri,
                    onImageSelected = { it?.let { onEvent(ReceiptDetailEvent.OnImageSelected(it)) } },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    onImageClick = { showPreviewDialog = true },
                )
                ImageUploaderButton(uiState.selectedImageUri, uiState.uploadedImageUri) { onEvent(ReceiptDetailEvent.OnClickImageUpload) }
            }
        }

        // 位置情報セクション
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(
                    text = "位置情報",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                CurrentLocationComponent(location = uiState.location) { it?.let { onEvent(ReceiptDetailEvent.OnLocationSet(it)) } }
                if (uiState.imageLocation != Location.Empty) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Button(
                            onClick = {
                                val location = uiState.imageLocation
                                onEvent(ReceiptDetailEvent.OnLocationSet(location))
                            },
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            Text(context.getString(R.string.use_image_location))
                        }
                        Text("${uiState.imageLocation.latitude}, ${uiState.imageLocation.longitude}")
                    }
                }
                MapLocationFinder(
                    uiState.location ?: Location.Default,
                    onLocationSelected = { onEvent(ReceiptDetailEvent.OnLocationSet(it)) },
                )
            }
        }

        if (uiState.loading) {
            Text("Analyzing receipt info ...", modifier = Modifier.padding(horizontal = 16.dp))
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }

        // レシート情報セクション
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            ReceiptInfoPane(uiState)
        }

        // 操作セクション
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Button(
                        onClick = { onEvent(ReceiptDetailEvent.OnClickTest) },
                        shape = RoundedCornerShape(8.dp),
                    ) { Text(text = "Test") }

                    ActionButton(
                        text = "共有",
                        onClick = {
                            val title = uiState.receipt.content.title()
                            val text = uiState.receipt.json()
                            val shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, text)
                                putExtra(Intent.EXTRA_SUBJECT, title)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "共有"))
                        },
                        enabled = uiState.receipt != ReceiptMetaData.Empty,
                    )
                    ActionButton(
                        text = "保存",
                        onClick = { onEvent(ReceiptDetailEvent.OnSaveMetaData(uiState.receipt)) },
                        enabled = uiState.saved.not(),
                    )
                }
                Text(
                    text = "修正依頼",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
                OutlinedTextField(
                    value = uiState.correctionText,
                    onValueChange = { onEvent(ReceiptDetailEvent.OnCorrectionTextChanged(it)) },
                    label = { Text("修正内容") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { onEvent(ReceiptDetailEvent.OnCorrectionSubmitted(uiState.correctionText)) },
                    enabled = uiState.correctionText.isNotBlank(),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("修正を送信")
                }
            }
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    content: @Composable () -> Unit = { Text(text) },
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        shape = RoundedCornerShape(8.dp),
    ) {
        content()
    }
}

@Preview(showBackground = true, heightDp = 2000)
@Composable
fun ReceiptDetailPreview() {
    val context = LocalContext.current
    val uiState = ReceiptDetailUiState(
        selectedImageUri = "android.resource://${context.packageName}/drawable/receipt_sample".toUri(),
        uploadedImageUri = Uri.Builder().authority("example.com").build(),
        receipt = ReceiptMetaData.Sample,
        loading = false,
        location = Location.Default,
        saved = false,
        correctionText = "",
    )
    Column(
        modifier = Modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.Top,
    ) {
        ReceiptDetail(uiState, {})
    }
}