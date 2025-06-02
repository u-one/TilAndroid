package net.uoneweb.android.til

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.coroutineScope
import com.google.gson.JsonParser
import kotlinx.coroutines.launch
import net.uoneweb.android.til.ui.receipt.data.ReceiptMetaData
import net.uoneweb.android.til.ui.receipt.repository.ReceiptMetaDataRepository
import java.io.BufferedReader
import java.io.InputStreamReader

class ShareReceiverActivity : ComponentActivity() {

    lateinit var receiptMetaDataRepository: ReceiptMetaDataRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        receiptMetaDataRepository = ReceiptMetaDataRepository(application)

        Log.d("ShareReceiverActivity", intent.toString())
        try {
            val intent = intent
            if (intent?.action == Intent.ACTION_SEND) {
                handleActionSend(intent)
            }
            if (intent?.action == Intent.ACTION_VIEW) {
                handleActionView(intent)
            }

        } catch (e: Exception) {
            Log.e("FileOpenActivity", "Error: ", e)
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }

        // 必要に応じてUIを表示
        finish() // UIを表示しない場合はActivityを終了
    }

    fun handleActionSend(intent: Intent) {
        if (intent.type != "text/plain") {
            return
        }

        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (sharedText != null) {
            Toast.makeText(this, "Received: $sharedText", Toast.LENGTH_LONG).show()
        }
    }

    fun handleActionView(intent: Intent) {
        val uri: Uri? = intent.data
        if (uri == null) {
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
            if (sharedText != null) {
                Toast.makeText(this, "Received: $sharedText", Toast.LENGTH_LONG).show()
            }
            return
        }
        if (intent.type == "text/plain") {
            val text = readString(uri)
            Toast.makeText(this, "File content:\n${text}", Toast.LENGTH_LONG).show()
            lifecycle.coroutineScope.launch {
                handleJson(text)
            }
        }
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
            // multiple receipts
            val array = jsonObj.getAsJsonArray("receipts")
            for (receipt in array) {
                val data = ReceiptMetaData.fromJson(receipt.toString())
                receiptMetaDataRepository.insert(data)
            }
            return
        }
        if (jsonObj.has("meta")) {
            // v3
            val data = ReceiptMetaData.fromJson(text)
            receiptMetaDataRepository.insert(data)
            return
        } else if (jsonObj.has("receipt")) {
            // v2, v1
            val data = ReceiptMetaData.fromJson(text)
            receiptMetaDataRepository.insert(data)
            return
        }
    }
}