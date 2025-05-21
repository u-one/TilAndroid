package net.uoneweb.android.til.ui.receipt.data

import com.google.gson.JsonParser

class Receipt(jsonStr: String) {
    val version = "v3"

    val json: String = jsonStr
    val date: String
    val time: String
    val storeName: String
    val storeBranch: String
    val total: Int
    val address: String

    init {
        if (jsonStr.isEmpty()) {
            date = ""
            time = ""
            storeName = "NA"
            storeBranch = ""
            total = 0
            address = ""
        } else {
            val jsonObj = JsonParser.parseString(jsonStr)?.asJsonObject
            val storeObj = jsonObj?.get("store")?.asJsonObject
            val receiptObj = jsonObj?.get("receipt")?.asJsonObject
            date = receiptObj?.get("date")?.asString
                ?.replace("-", "")
                ?: ""
            time = receiptObj?.get("time")?.asString
                ?.replace(":", "")
                ?: ""
            storeName = storeObj?.get("name")?.asString ?: "NA"
            storeBranch = storeObj?.get("branch")?.asString ?: ""
            total = jsonObj?.get("total")?.asInt ?: 0
            address = storeObj?.get("address")?.asString ?: ""
        }
    }

    fun title(): String {
        if (storeBranch.isNotBlank()) {
            return "${date}_${time}_${total}_${version}_${storeName}_${storeBranch}.json"
        }
        return "${date}_${time}_${total}_${version}_${storeName}.json"
    }

    fun store(): String {
        return if (storeBranch.isNotBlank()) {
            "$storeName $storeBranch"
        } else {
            storeName
        }
    }

    fun total(): Int {
        return total
    }

    fun address(): String {
        return address
    }

    companion object {

        val Empty = Receipt("")

        val Sample = Receipt(
            """
            {
              "store": {
                "name": "store",
                "branch": "branch"
              },
              "receipt": {
                "date": "2025-01-01",
                "time": "12:34"
              },
              "total": 5678 
            }
        """.trimIndent(),
        )
    }
}

