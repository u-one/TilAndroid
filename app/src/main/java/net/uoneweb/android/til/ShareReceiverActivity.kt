package net.uoneweb.android.til

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.launch
import net.uoneweb.android.til.ui.receipt.data.ReceiptMetaData
import net.uoneweb.android.til.ui.receipt.repository.ReceiptMetaDataRepository
import java.io.BufferedReader
import java.io.InputStreamReader

class ShareReceiverActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val receiptMetaDataRepository = ReceiptMetaDataRepository(application)

        // Intentを取得
        val intent = intent
        if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
            if (sharedText != null) {
                // 受け取ったテキストを処理
                Toast.makeText(this, "Received: $sharedText", Toast.LENGTH_LONG).show()
            }
        }
        if (intent?.action == Intent.ACTION_VIEW) {
            val uri: Uri? = intent.data
            if (uri != null) {
                if (intent.type == "text/plain") {
                    try {
                        contentResolver.openInputStream(uri)?.use { inputStream ->
                            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                                val stringBuilder = StringBuilder()
                                var line: String?
                                while (reader.readLine().also { line = it } != null) {
                                    stringBuilder.append(line).append("\n")
                                }
                                Toast.makeText(this, "File content:\n${stringBuilder.toString()}", Toast.LENGTH_LONG).show()

                                lifecycle.coroutineScope.launch {
                                    val data = ReceiptMetaData.fromJson(stringBuilder.toString())
                                    receiptMetaDataRepository.insert(data)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("FileOpenActivity", "Error reading file", e)
                        Toast.makeText(this, "Error reading file: ${e.message}", Toast.LENGTH_LONG).show()
                    }

                }
            } else {
                val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
                if (sharedText != null) {
                    Toast.makeText(this, "Received: $sharedText", Toast.LENGTH_LONG).show()
                }
            }
        }

        // 必要に応じてUIを表示
        finish() // UIを表示しない場合はActivityを終了
    }
}