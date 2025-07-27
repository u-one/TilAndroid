package net.uoneweb.android.receipt.ui

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import net.uoneweb.android.gis.ui.location.Location
import net.uoneweb.android.mapping.ui.ReceiptMappingDetail
import net.uoneweb.android.mapping.ui.ReceiptMappingEvent
import net.uoneweb.android.mapping.ui.ReceiptMappingList
import net.uoneweb.android.mapping.ui.ReceiptMappingUiState
import net.uoneweb.android.receipt.data.Receipt
import net.uoneweb.android.receipt.data.ReceiptMappingInfo
import net.uoneweb.android.receipt.data.ReceiptMetaData
import net.uoneweb.android.receipt.ui.detail.ReceiptDetail
import net.uoneweb.android.receipt.ui.detail.ReceiptDetailEvent
import net.uoneweb.android.receipt.ui.detail.ReceiptDetailUiState
import net.uoneweb.android.receipt.ui.list.ReceiptList

@Composable
fun ReceiptScreen(viewModel: ReceiptViewModel = viewModel()) {
    val receiptMappingInfo by viewModel.osmInfoJson
    val receiptMetaDataList = viewModel.listReceiptMetaData.collectAsState()
    //val receiptMappingInfoList = viewModel.listReceiptMappingInfosByReceiptId(receipt.id ?: 0).collectAsState()
    val receiptMappingInfoList = viewModel.listReceiptMappingInfos().collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val loadingFeature by viewModel.loadingFeature

    val receiptDetailUiState by viewModel.receiptDetailUiState.collectAsState()

    val receiptMappingUiState = ReceiptMappingUiState(
        isLoading = loadingFeature,
        receiptMappingInfo = receiptMappingInfo,
        location = receiptDetailUiState.receipt.location ?: Location.Default,
    )

    Column {
        ReceiptScreenMain(
            list = receiptMetaDataList.value,
            receiptMappingInfoList = receiptMappingInfoList.value,
            onClickItem = { item ->
                item.id?.let { id ->
                    viewModel.select(id)
                }
            },
            receiptDetailUiState = receiptDetailUiState,
            onReceiptDetailEvent = { event ->
                coroutineScope.launch {
                    when (event) {
                        is ReceiptDetailEvent.OnImageSelected -> {
                            viewModel.generateJsonFromImage(event.uri)
                        }

                        is ReceiptDetailEvent.OnLocationSet -> {
                            viewModel.setLocation(event.location)
                        }

                        is ReceiptDetailEvent.OnSaveMetaData -> {
                            viewModel.saveReceiptMetaData(event.metaData)
                        }

                        is ReceiptDetailEvent.OnClickImageUpload -> {
                            receiptDetailUiState.selectedImageUri?.let { uri ->
                                viewModel.uploadImage(uri)
                            }
                        }

                        is ReceiptDetailEvent.OnClickTest -> {
                            viewModel.receiptResultTest()
                        }

                        is ReceiptDetailEvent.OnCorrectionSubmitted -> {
                            viewModel.correctReceiptInfo(event.correction)
                        }

                        is ReceiptDetailEvent.OnCorrectionTextChanged -> {
                            viewModel.updateCorrectionText(event.text)
                        }
                    }
                }
            },
            receiptMappingUiState = receiptMappingUiState,
            onReceiptMappingEvent = viewModel::onReceiptMappingEvent,
        )
    }
}

@Composable
fun ReceiptScreenMain(
    list: List<ReceiptMetaData> = emptyList(),
    receiptMappingInfoList: List<ReceiptMappingInfo> = emptyList(),
    onClickItem: (item: ReceiptMetaData) -> Unit = {},
    receiptDetailUiState: ReceiptDetailUiState = ReceiptDetailUiState(),
    onReceiptDetailEvent: (receiptDetailEvent: ReceiptDetailEvent) -> Unit = {},
    receiptMappingUiState: ReceiptMappingUiState = ReceiptMappingUiState(),
    onReceiptMappingEvent: (ReceiptMappingEvent) -> Unit = {},
) {

    val scrollState = rememberScrollState()

    var selectedTabIndex by remember { mutableStateOf(0) }
    var showPickerDialog by remember { mutableStateOf(false) }

    val imagePickerDialogState = rememberImagePickerDialogState(
        onImageSelected = {
            onReceiptDetailEvent(ReceiptDetailEvent.OnImageSelected(it))
            selectedTabIndex = 1
        },
    )

    Box {
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                val tabTitles = listOf("List", "ReceiptInfo", "MappingInfoList", "MappingInfo")
                tabTitles.forEachIndexed { index, title ->
                    androidx.compose.material.Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) },
                    )
                }
            }
            when (selectedTabIndex) {
                0 -> {
                    ReceiptList(
                        list,
                        onClickItem = {
                            onClickItem(it)
                            selectedTabIndex = 1
                        },
                    )
                }

                1 -> {
                    ReceiptDetail(receiptDetailUiState, onReceiptDetailEvent)
                }

                2 -> {
                    ReceiptMappingList(
                        receiptMappingInfoList,
                        onClickItem = {
                            // TODO: load
                            selectedTabIndex = 3
                        },
                    )
                }

                3 -> {
                    ReceiptMappingDetail(
                        uiState = receiptMappingUiState,
                        onEvent = onReceiptMappingEvent,
                    )
                }
            }
        }
        FloatingActionButton(
            onClick = { showPickerDialog = true },
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.BottomEnd)
                .padding(16.dp),
        ) {
            Icon(Icons.Default.Add, contentDescription = "レシート追加")
        }
        if (showPickerDialog) {
            ImagePickerDialog(
                state = imagePickerDialogState,
                onDismissRequest = {
                    showPickerDialog = false
                },
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun ReceiptScreenMainProgressPreview() {
    val context = LocalContext.current

    val receiptDetailUiState = ReceiptDetailUiState(
        selectedImageUri = "android.resource://${context.packageName}/drawable/dummy_receipt".toUri(),
        uploadedImageUri = Uri.Builder().build(),
        receipt = ReceiptMetaData.Empty,
        currentLocation = null,
        loading = true,
    )

    ReceiptScreenMain(
        receiptDetailUiState = receiptDetailUiState,
    )
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun ReceiptScreenMainPreview() {
    val context = LocalContext.current

    val receiptDetailUiState = ReceiptDetailUiState(
        selectedImageUri = "android.resource://${context.packageName}/drawable/dummy_receipt".toUri(),
        uploadedImageUri = Uri.Builder().authority("example.com").build(),
        receipt = ReceiptMetaData(Receipt.Sample),
        currentLocation = null,
        loading = true,
    )

    ReceiptScreenMain(
        list = listOf(
            ReceiptMetaData(Receipt.Sample),
            ReceiptMetaData(Receipt.Empty),
        ),
        receiptDetailUiState = receiptDetailUiState,
    )
}
