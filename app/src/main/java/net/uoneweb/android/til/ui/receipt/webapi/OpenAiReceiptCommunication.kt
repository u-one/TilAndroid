package net.uoneweb.android.til.ui.receipt.webapi

import android.content.Context
import net.uoneweb.android.til.R
import java.io.BufferedReader
import java.io.InputStreamReader

object OpenAiReceiptPrompt {

    fun promptToInferOsmFeatureFromReceipt(context: Context, receiptJson: String): String {
        val inputStream = context.resources.openRawResource(R.raw.osm_data_from_receipt_json)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val template = reader.use { it.readText() }
        return template.replace("{{json}}", receiptJson)
    }

    fun promptToInferOsmFeatureFromReceiptAndActualFeature(context: Context, receiptJson: String, featureGeoJson: String): String {
        val inputStream = context.resources.openRawResource(R.raw.prompt_infer_osm_feature_from_receipt_and_actual_feature)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val template = reader.use { it.readText() }
        return template.replace("{{receipt_json}}", receiptJson)
            .replace("{{existing_geojson}}", featureGeoJson)
    }
}

object SampleData {
    fun dummyData(context: Context): String {
        val inputStream = context.resources.openRawResource(R.raw.receipt_sample)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.use { it.readText() }
    }


    fun responseSample(context: Context): String {
        //val inputStream = context.resources.openRawResource(R.raw.osm_data_from_receipt_response_sample)
        val inputStream = context.resources.openRawResource(R.raw.receipt_mapping_response_with_actual_sample1)
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

