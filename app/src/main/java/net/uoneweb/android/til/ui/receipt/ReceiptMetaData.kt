package net.uoneweb.android.til.ui.receipt

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser

data class Location(
    val latitude: Double,
    val longitude: Double,
)

data class ReceiptMetaData(
    val content: Receipt,
    val location: Location? = null,
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
}


