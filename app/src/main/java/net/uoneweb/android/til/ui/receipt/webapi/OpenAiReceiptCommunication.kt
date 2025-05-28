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

    fun shortSample(): String {
        return """
 {
  "store_info_extracted": [
    {
      "key": "name",
      "value": "廣榮堂 倉敷店",
      "based_on": "fact",
      "reason": "store.nameとbranch情報を結合したため",
      "comment": "店舗の正式名称。レシートJSONから直接取得した確実な情報。"
    },
    {
      "key": "shop",
      "value": "confectionery",
      "based_on": "inference",
      "reason": "商品欄から和菓子販売が主体と推測（吉備団子）",
      "comment": "和菓子を含む菓子（主に土産物）専門店と推測。"
    }
  ],
  "existing_store_info": {
    "tags": [
      {
        "key": "name",
        "value": "廣榮堂本店",
        "osm_id": "36481525031",
        "comment": "OpenStreetMap上に登録されている店舗名。恐らく本店表記。"
      },
      {
        "key": "class",
        "value": "restaurant",
        "osm_id": "36481525031",
        "comment": "施設分類: レストラン。"
      }
    ],
    "comment": "現在OSM上では廣榮堂本店（本店）がポイント地物（ID:36481525031）として登録あり。「restaurant」扱いになっている。"
  },
  "comparison": [
    {
      "key": "shop",
      "existing_value": null,
      "new_value": "confectionery",
      "difference": "new",
      "comment": "既存にshopタグなし。和菓子土産系の業態を反映すべき。"
    },
    {
      "key": "class",
      "existing_value": "restaurant",
      "new_value": null,
      "difference": "different",
      "comment": "既存はclass=restaurantで登録。レシート店舗は物販型。業態の違いに注意。"
    }
  ],
  "update_recommendation": "既存のOSMポイント（ID:36481525031）は「廣榮堂本店」で、今回のレシートは「廣榮堂 倉敷店」（別支店）である。地図上の位置が一致する場合は、本店表記を現店舗名・業態（shop=confectionery等）で更新、業態を『和菓子販売（和菓子店、土産物）』へ修正することが望ましい。異なる場所・支店であれば新規POIとして上記情報で追加するのが最適。支払い方法・公式連絡先などの付与も推奨。"
}
    """.trimIndent()
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

