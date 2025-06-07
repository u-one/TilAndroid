package net.uoneweb.android.til.ui.receipt.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.uoneweb.android.gis.ui.location.Location


data class ReceiptMetaData(
    val content: Receipt,
    val id: Long? = null,
    val location: Location? = null,
    val filename: String? = null,
) {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun json(): String {
        val metaObj = JsonObject()
        metaObj.addProperty("version", content.version)
        if (location != null) {
            val locationObj = JsonObject()
            locationObj.addProperty("latitude", location.latitude.value)
            locationObj.addProperty("longitude", location.longitude.value)
            metaObj.add("location", locationObj)
        }
        if (filename != null) {
            metaObj.addProperty("filename", filename)
        }

        val jsonObj = JsonObject()
        val receiptObj = JsonParser.parseString(content.json)
        jsonObj.add("receipt", receiptObj)
        jsonObj.add("meta", metaObj)

        return gson.toJson(jsonObj)
    }

    companion object {
        val Empty = ReceiptMetaData(Receipt.Empty)
        val Sample = ReceiptMetaData(
            content = Receipt.Sample,
            id = 1L,
            location = Location(12.34, 56.78),
            filename = "sample_receipt.json",
        )


        fun fromJson(json: String): ReceiptMetaData {
            val gson = Gson()
            val jsonObj = JsonParser.parseString(json).asJsonObject

            if (jsonObj.has("meta")) {
                val metaObj = jsonObj.getAsJsonObject("meta")
                val id = if (metaObj.has("id")) {
                    metaObj.get("id").asLong
                } else null

                val location = if (metaObj.has("location")) {
                    val locationObj = metaObj.getAsJsonObject("location")
                    Location(
                        locationObj.get("latitude").asDouble,
                        locationObj.get("longitude").asDouble,
                    )
                } else null

                val filename = if (metaObj.has("filename")) {
                    metaObj.get("filename").asString
                } else null

                val receiptObj = jsonObj.getAsJsonObject("receipt")
                val receipt = Receipt(gson.toJson(receiptObj))
                return ReceiptMetaData(receipt, id, location, filename)
            } else if (jsonObj.has("receipt")) {
                // v2, v1
                val receiptObj = jsonObj.getAsJsonObject("receipt")
                val receipt = Receipt(gson.toJson(receiptObj))
                return ReceiptMetaData(receipt)
            } else {
                throw IllegalArgumentException("Invalid JSON format for ReceiptMetaData")

            }
        }
    }
}


