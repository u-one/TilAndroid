package net.uoneweb.android.til.ui.receipt

import com.google.gson.JsonParser

class Receipt(private val json: String) {

    fun title(): String {
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
}