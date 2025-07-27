package net.uoneweb.android.til

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
import kotlinx.coroutines.launch
import net.uoneweb.android.receipt.data.ReceiptMetaData
import net.uoneweb.android.receipt.repository.ReceiptMetaDataRepository

class ShareReceiverActivity : AppCompatActivity() {
    lateinit var receiptMetaDataRepository: ReceiptMetaDataRepository
    lateinit var event: IntentEvent

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiptMetaDataRepository = ReceiptMetaDataRepository(application)

        Log.d("ShareReceiverActivity", intent.toString())
        try {
            event = IntentParser(contentResolver).parse(intent)
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
                } else {
                    handleEvent(event)
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
                                        handleEvent(event)
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

    suspend fun handleEvent(event: IntentEvent) {
        when (event) {
            is IntentEvent.MultipleReceipts -> {
                Log.d("ShareReceiverActivity", "MultipleReceipts: ${event.data().size} items")
                saveMultipleReceipts(event.data())
            }

            is IntentEvent.ReceiptV3 -> {
                Log.d("ShareReceiverActivity", "ReceiptV3: ${event.data()}")
                receiptMetaDataRepository.insert(event.data())
            }

            is IntentEvent.ReceiptV2 -> {
                Log.d("ShareReceiverActivity", "ReceiptV2: ${event.data()}")
                receiptMetaDataRepository.insert(event.data())
            }

            is IntentEvent.Unknown -> {
                Log.e("ShareReceiverActivity", "Unknown event: ${event.text}")
                Toast.makeText(this@ShareReceiverActivity, "Unknown event: ${event.text}", Toast.LENGTH_LONG).show()
            }
        }
    }

    suspend fun saveMultipleReceipts(list: List<ReceiptMetaData>) {
        var cnt = 0
        for (receipt in list) {
            val data = ReceiptMetaData.fromJson(receipt.toString())
            val id = receiptMetaDataRepository.insert(data)
            if (id >= 0) {
                cnt++
                Log.d("handleJson", "Inserted receipt with ID: $id")
            } else {
                Log.e("handleJson", "Failed to insert receipt: $data")
            }
        }
        Log.d("handleJson", "Inserted $cnt/${list.size} receipts")
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
