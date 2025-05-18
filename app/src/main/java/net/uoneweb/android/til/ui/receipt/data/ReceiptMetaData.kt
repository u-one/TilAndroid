package net.uoneweb.android.til.ui.receipt.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser


data class ReceiptMetaData(
    val content: Receipt,
    val id: Long? = null,
    val location: net.uoneweb.android.gis.ui.location.Location? = null,
    val filename: String? = null,
) {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun json(): String {


        val metaObj = JsonObject()
        metaObj.addProperty("version", content.version)
        if (location != null) {
            val locationObj = JsonObject()
            locationObj.addProperty("latitude", location.latitude)
            locationObj.addProperty("longitude", location.longitude)
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


        fun fromJson(json: String): ReceiptMetaData {
            val gson = Gson()
            val jsonObj = JsonParser.parseString(json).asJsonObject
            val receiptObj = jsonObj.getAsJsonObject("receipt")
            val receipt = Receipt(gson.toJson(receiptObj))

            val metaObj = jsonObj.getAsJsonObject("meta")
            val id = if (metaObj.has("id")) {
                metaObj.get("id").asLong
            } else null

            val location = if (metaObj.has("location")) {
                val locationObj = metaObj.getAsJsonObject("location")
                net.uoneweb.android.gis.ui.location.Location(
                    locationObj.get("latitude").asDouble,
                    locationObj.get("longitude").asDouble,
                )
            } else null


            val filename = if (metaObj.has("filename")) {
                metaObj.get("filename").asString
            } else null

            return ReceiptMetaData(receipt, id, location, filename)
        }
    }
}


