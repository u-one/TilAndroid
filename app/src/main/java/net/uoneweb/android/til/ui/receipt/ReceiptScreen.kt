package net.uoneweb.android.til.ui.receipt

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import net.uoneweb.android.til.ui.location.CurrentLocationComponent
import net.uoneweb.android.til.ui.location.Location
import net.uoneweb.android.til.ui.receipt.data.Receipt
import net.uoneweb.android.til.ui.receipt.data.ReceiptMappingInfo
import net.uoneweb.android.til.ui.receipt.data.ReceiptMetaData
import net.uoneweb.android.til.ui.receipt.webapi.SampleData

@Composable
fun ReceiptScreen(viewModel: ReceiptViewModel = viewModel()) {
    val selectedImageUri = rememberSaveable { mutableStateOf<Uri?>(null) }
    val uploadedImageUri by viewModel.uploadedFileUrl
    val receipt by viewModel.receipt
    val coroutineScope = rememberCoroutineScope()
    val receiptMappingInfo by viewModel.json
    val context = LocalContext.current
    val receiptMetaDataList = viewModel.listReceiptMetaData.collectAsState()
    val receiptMappingInfoList = viewModel.listReceiptMappingInfosByReceiptId(receipt.id ?: 0).collectAsState()

    val loading = (selectedImageUri.value != null && receipt == ReceiptMetaData.Empty)

    //OpenAiUi()
    //AiResult()
    LaunchedEffect(selectedImageUri.value) {
        println("selectedImageUri: $selectedImageUri")
        if (selectedImageUri.value != null) {
            viewModel.generateJsonFromImage(selectedImageUri.value!!)
        }
    }

    Column {
        ReceiptScreenMain(
            list = receiptMetaDataList.value,
            receiptMappingInfoList = receiptMappingInfoList.value,
            onClickItem = { item ->
                item.id?.let { id ->
                    viewModel.select(id)
                }
            },
            onSaveMetaData = {
                viewModel.saveReceiptMetaData(it)
            },
            selectedImageUri = selectedImageUri.value,
            uploadedImageUri = uploadedImageUri,
            receipt = receipt,
            receiptMappingInfo = receiptMappingInfo,
            onImageSelected = { uri ->
                selectedImageUri.value = uri
                viewModel.reset()
            },
            onClickReceiptResultTest = {
                viewModel.receiptResultTest()
            },
            onClickImageUpload = {
                selectedImageUri.value?.let {
                    viewModel.uploadImage(it)
                }
            },
            onClickOsmInfo = { isTest ->
                coroutineScope.launch {
                    val json = if (receipt == ReceiptMetaData.Empty) {
                        SampleData.dummyData(context)
                    } else {
                        receipt.content.json
                    }
                    viewModel.generateOsmInfoFromJson(json, isTest)
                }
            },
            loading = loading,
        )
    }
}

@Composable
fun ReceiptScreenMain(
    list: List<ReceiptMetaData> = emptyList(),
    receiptMappingInfoList: List<ReceiptMappingInfo> = emptyList(),
    onClickItem: (item: ReceiptMetaData) -> Unit = {},
    onSaveMetaData: (ReceiptMetaData) -> Unit = {},
    selectedImageUri: Uri? = null,
    uploadedImageUri: Uri? = null,
    receipt: ReceiptMetaData = ReceiptMetaData.Empty,
    onClickReceiptResultTest: () -> Unit = {},
    receiptMappingInfo: String = "",
    onImageSelected: (Uri?) -> Unit = {},
    onClickImageUpload: () -> Unit = {},
    onClickOsmInfo: (isTest: Boolean) -> Unit = {},
    loading: Boolean = false,
) {

    val scrollState = rememberScrollState()
    Column(modifier = Modifier.verticalScroll(scrollState)) {
        var selectedTabIndex by remember { mutableStateOf(0) }
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
                list.forEach { item ->
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                selectedTabIndex = 1
                                onClickItem(item)
                            },
                    ) {
                        Text(item.content.title())
                    }
                }
            }

            1 -> {
                var location by remember { mutableStateOf<Location?>(null) }
                Row {
                    ImageSelector(selectedImageUri) { onImageSelected(it) }
                    Button(onClick = onClickReceiptResultTest) { Text(text = "ReceiptResultTest") }
                }
                ImageUploaderButton(selectedImageUri, uploadedImageUri) { onClickImageUpload() }
                CurrentLocationComponent(location = location) { location = it }

                if (loading) {
                    Text("Analyzing receipt info ...")
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                ReceiptInfoPane(receipt, selectedImageUri, location, onSaveMetaData)
            }

            2 -> {
                receiptMappingInfoList.forEach { item ->
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                selectedTabIndex = 3
                            },
                    ) {
                        Text(item.id.toString())
                    }

                }
            }

            3 -> {
                Row {
                    Button(
                        onClick = { onClickOsmInfo(false) },
                    ) {
                        Text("ReceiptMappingInfo")
                    }
                    Button(
                        onClick = { onClickOsmInfo(true) },
                    ) {
                        Text("TestReceiptMappingInfo")
                    }
                }
                ReceiptMappingPane(receiptMappingInfo)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun ReceiptScreenMainProgressPreview() {
    val context = LocalContext.current
    val selectedImageUri = "android.resource://${context.packageName}/drawable/dummy_receipt".toUri()
    val uploadedImageUri = Uri.Builder().build()

    ReceiptScreenMain(
        selectedImageUri = selectedImageUri,
        uploadedImageUri = uploadedImageUri,
        loading = true,
    )
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun ReceiptScreenMainPreview() {
    val context = LocalContext.current
    val selectedImageUri = "android.resource://${context.packageName}/drawable/dummy_receipt".toUri()
    val uploadedImageUri = Uri.Builder().authority("example.com").build()
    val receipt = Receipt(
        """
            {
              "store": {
                "name": "store",
                "branch": "branch"
              },
              "receipt": {
                "date": "2025-01-01",
                "time": "12:34"
              },
              "total": 5678 
            }
        """.trimIndent(),
    )

    ReceiptScreenMain(
        list = listOf(
            ReceiptMetaData(Receipt.Empty),
            ReceiptMetaData(Receipt.Empty),
        ),
        selectedImageUri = selectedImageUri,
        uploadedImageUri = uploadedImageUri,
        receipt = ReceiptMetaData(receipt),

        )
}

