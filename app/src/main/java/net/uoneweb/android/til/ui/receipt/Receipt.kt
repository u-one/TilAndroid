package net.uoneweb.android.til.ui.receipt

import com.google.gson.JsonParser

class Receipt(val json: String) {

    fun title(): String {
        if (json.isBlank()) return ""
        val jsonObj = JsonParser.parseString(json).asJsonObject
        val storeObj = jsonObj.get("store")?.asJsonObject
        val receiptObj = jsonObj.get("receipt")?.asJsonObject

        val date = receiptObj?.get("date")?.asString
            ?.replace("-", "")
            ?: ""
        val time = receiptObj?.get("time")?.asString
            ?.replace(":", "")
            ?: ""

        val storeName = storeObj?.get("name")?.asString ?: "NA"
        val storeBranch = storeObj?.get("branch")?.asString ?: ""

        val total = jsonObj.get("total")?.asString ?: "0"

        if (storeBranch.isNotBlank()) {
            return "${date}_${time}_${total}_${storeName}_${storeBranch}.json"
        }
        return "${date}_${time}_${total}_${storeName}.json"
    }

    fun store(): String {
        if (json.isBlank()) return ""
        val jsonObj = JsonParser.parseString(json).asJsonObject
        val storeObj = jsonObj.get("store")?.asJsonObject
        val storeName = storeObj?.get("name")?.asString ?: "NA"
        val storeBranch = storeObj?.get("branch")?.asString ?: ""
        return if (storeBranch.isNotBlank()) {
            "$storeName $storeBranch"
        } else {
            storeName
        }
    }

    fun total(): Int {
        if (json.isBlank()) return 0
        val jsonObj = JsonParser.parseString(json).asJsonObject
        return jsonObj.get("total")?.asInt ?: 0
    }

    companion object {
        val Empty = Receipt("")
    }

}

