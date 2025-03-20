package net.uoneweb.android.til.ui.receipt

import org.json.JSONException
import org.json.JSONObject

data class ResponseParser(
    val text: String?,
) {
    fun json(): String {
        if (text == null) {
            return ""
        }
        return formatJson(removeAffix(text))
    }

    fun formatJson(text: String?): String {
        if (text == null) {
            return ""
        }
        return try {
            val jsonObj = JSONObject(text)
            jsonObj.toString(4)
        } catch (e: JSONException) {
            e.printStackTrace()
            text
        }
    }

    fun removeAffix(text: String): String {
        return text.substringBeforeLast("```")
            .substringAfter("```json")
            .trim()
    }
}