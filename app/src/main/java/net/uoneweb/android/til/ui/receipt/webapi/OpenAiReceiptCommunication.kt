package net.uoneweb.android.til.ui.receipt.webapi

import android.content.Context
import net.uoneweb.android.til.R
import java.io.BufferedReader
import java.io.InputStreamReader

object OpenAiReceiptPrompt {

    fun loadOsmFromJsonPrompt(context: Context): String {
        val inputStream = context.resources.openRawResource(R.raw.osm_data_from_receipt_json)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.use { it.readText() }
    }
}

object SampleData {
    fun dummyData(context: Context): String {
        val inputStream = context.resources.openRawResource(R.raw.receipt_sample)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.use { it.readText() }
    }


    fun responseSample(context: Context): String {
        val inputStream = context.resources.openRawResource(R.raw.osm_data_from_receipt_response_sample)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.use { it.readText() }
    }
}

data class OpenAiReceiptResponse(
    val text: String?,
) {
    fun json(): String {
        if (text == null) {
            return ""
        }
        return text
    }
}

