package net.uoneweb.android.til

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import com.google.gson.JsonParser
import net.uoneweb.android.receipt.data.ReceiptMetaData
import java.io.BufferedReader
import java.io.InputStreamReader

sealed class IntentEvent() {
    fun text(): String {
        return when (this) {
            is MultipleReceipts -> text
            is ReceiptV3 -> text
            is ReceiptV2 -> text
            is Unknown -> text
        }
    }

    data class MultipleReceipts(val text: String) : IntentEvent() {
        fun data(): List<ReceiptMetaData> {
            val jsonObj = JsonParser.parseString(text).asJsonObject
            val array = jsonObj.getAsJsonArray("receipts")
            return array.map { ReceiptMetaData.fromJson(it.toString()) }
        }
    }

    data class ReceiptV3(val text: String) : IntentEvent() {
        fun data(): ReceiptMetaData {
            return ReceiptMetaData.fromJson(text)
        }
    }

    data class ReceiptV2(val text: String) : IntentEvent() {
        fun data(): ReceiptMetaData {
            return ReceiptMetaData.fromJson(text)
        }
    }

    data class Unknown(val text: String = "") : IntentEvent()
}

class IntentParser(private val contentResolver: ContentResolver) {
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
                        // v3
                        return IntentEvent.ReceiptV3(text)
                    } else if (jsonObj.has("receipt")) {
                        // v2, v1
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
        return IntentEvent.Unknown("unsupported type: $intent")
    }

    fun readString(uri: Uri): String {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                return reader.readText()
            }
        } ?: throw IllegalArgumentException("Unable to open input stream for URI: $uri")
    }
}
