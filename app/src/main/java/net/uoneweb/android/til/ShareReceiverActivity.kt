package net.uoneweb.android.til

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.coroutineScope
import com.google.gson.JsonParser
import kotlinx.coroutines.launch
import net.uoneweb.android.til.ui.receipt.data.ReceiptMetaData
import net.uoneweb.android.til.ui.receipt.repository.ReceiptMetaDataRepository
import java.io.BufferedReader
import java.io.InputStreamReader

class ShareReceiverActivity : AppCompatActivity() {

    lateinit var receiptMetaDataRepository: ReceiptMetaDataRepository
    lateinit var event: IntentEvent

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiptMetaDataRepository = ReceiptMetaDataRepository(application)

        Log.d("ShareReceiverActivity", intent.toString())
        try {
            event = parse(intent)
        } catch (e: Exception) {
            Log.e("ShareReceiverActivity", "Error: ", e)
            Toast.makeText(this@ShareReceiverActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }

        setContent {
            var showDeleteAllDialog by remember { mutableStateOf(false) }
            val scrollState = rememberScrollState()

            LaunchedEffect(Unit) {
                if (event is IntentEvent.MultipleReceipts) {
                    showDeleteAllDialog = true
                }
            }

            MaterialTheme {
                Surface {
                    Column(modifier = Modifier.verticalScroll(scrollState)) {
                        if (showDeleteAllDialog) {
                            DeleteAllConfirmDialog(
                                onConfirm = {
                                    showDeleteAllDialog = false
                                    lifecycle.coroutineScope.launch {
                                        receiptMetaDataRepository.deleteAll()
                                        handleJson((event as IntentEvent.MultipleReceipts).text)
                                    }
                                },
                                onDismiss = {
                                    showDeleteAllDialog = false
                                },
                            )
                        }
                        if (event.text().isEmpty()) {
                            Text("No data received")
                        } else {
                            Text("Received data:")
                            Text(event.text())
                        }
                    }
                }
            }
        }


    }

    fun parse(intent: Intent?): IntentEvent {
        when (intent?.action) {
            Intent.ACTION_SEND -> {
                if (intent.type != "text/plain") {
                    return IntentEvent.Unknown("not text/plain")
                }
                return IntentEvent.Unknown(intent.getStringExtra(Intent.EXTRA_TEXT) ?: "empty text/plain")
            }

            Intent.ACTION_VIEW -> {
                val uri: Uri? = intent.data
                if (uri == null) {
                    return IntentEvent.Unknown(intent.getStringExtra(Intent.EXTRA_TEXT) ?: "no uri or text")
                }
                if (intent.type == "text/plain" || intent.type == "application/json") {
                    val text = readString(uri)
                    val jsonObj = JsonParser.parseString(text).asJsonObject
                    if (jsonObj.has("receipts")) {
                        return IntentEvent.MultipleReceipts(text)
                    } else if (jsonObj.has("meta")) {
                        return IntentEvent.ReceiptV3(text)
                    } else if (jsonObj.has("receipt")) {
                        return IntentEvent.ReceiptV2(text)
                    } else {
                        return IntentEvent.Unknown("unknown json format: $text")
                    }
                }
            }

            else -> {
                return IntentEvent.Unknown("unknown action: ${intent?.action}")
            }
        }
        return IntentEvent.Unknown("unsupported type: ${intent}")
    }

    fun readString(uri: Uri): String {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                return reader.readText()
            }
        } ?: throw IllegalArgumentException("Unable to open input stream for URI: $uri")
    }

    suspend fun handleJson(text: String) {
        val jsonObj = JsonParser.parseString(text).asJsonObject
        Log.d("handleJson", jsonObj.toString())
        if (jsonObj.has("receipts")) {
            saveMultipleReceipts(text)
            return
        } else if (jsonObj.has("meta")) {
            // v3
            val data = ReceiptMetaData.fromJson(text)
            receiptMetaDataRepository.insert(data)
            return
        } else if (jsonObj.has("receipt")) {
            // v2, v1
            val data = ReceiptMetaData.fromJson(text)
            receiptMetaDataRepository.insert(data)
            return
        } else {
            Log.e("handleJson", "Unknown JSON format: $text")
            Toast.makeText(this@ShareReceiverActivity, "Invalid JSON format", Toast.LENGTH_LONG).show()
            return
        }
    }

    suspend fun saveMultipleReceipts(json: String) {
        val jsonObj = JsonParser.parseString(json).asJsonObject
        val array = jsonObj.getAsJsonArray("receipts")
        val total = array.size()
        var cnt = 0
        for (receipt in array) {
            val data = ReceiptMetaData.fromJson(receipt.toString())
            val id = receiptMetaDataRepository.insert(data)
            if (id >= 0) {
                cnt++
                Log.d("handleJson", "Inserted receipt with ID: $id")
            } else {
                Log.e("handleJson", "Failed to insert receipt: $data")
            }
        }
        Log.d("handleJson", "Inserted $cnt/$total receipts")
    }
}

// すべて削除確認ダイアログ
@Composable
fun DeleteAllConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("確認") },
        text = { Text("すべて削除しますか？") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("キャンセル")
            }
        },
    )
}
