package net.uoneweb.android.receipt.webapi

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import net.uoneweb.android.receipt.R
import java.io.BufferedReader
import java.io.InputStreamReader

object GeminiReceiptPrompt {

    fun loadPrompt(context: Context): String {
        val inputStream = context.resources.openRawResource(R.raw.receipt_prompt)
        val reader = BufferedReader(InputStreamReader(inputStream))
        return reader.use { it.readText() }
    }

    fun loadCorrectionPrompt(context: Context, originalJson: String, correction: String): String {
        val basePrompt = loadPrompt(context)
        return """
            $basePrompt

            あなたは以前、この画像から以下の情報を抽出しました。
            ```json
            $originalJson
            ```

            しかし、ユーザーから以下の修正依頼がありました。
            """ + correction + """

            このフィードバックを考慮し、元の画像から正しい情報を再抽出し、修正されたJSON形式で出力してください。
            """
    }
}

data class GeminiReceiptResponse(
    val text: String?,
) {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun json(): String {
        if (text == null) {
            return ""
        }
        return formatJson(removeAffix(text))
    }

    private fun formatJson(text: String?): String {
        if (text == null) {
            return ""
        }
        return try {
            val jsonElem = JsonParser.parseString(text)
            gson.toJson(jsonElem)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            text
        }
    }

    private fun removeAffix(text: String): String {
        return text.substringBeforeLast("```")
            .substringAfter("```json")
            .trim()
    }
}