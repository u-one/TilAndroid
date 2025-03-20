package net.uoneweb.android.til.ui.receipt

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException

data class ResponseParser(
    val text: String?,
) {
    private val gson = GsonBuilder().setPrettyPrinting().create()

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
            val jsonElem = JsonParser.parseString(text)
            gson.toJson(jsonElem)
        } catch (e: JsonSyntaxException) {
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