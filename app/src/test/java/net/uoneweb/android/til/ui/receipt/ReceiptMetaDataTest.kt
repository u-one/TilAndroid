package net.uoneweb.android.til.ui.receipt

import com.google.gson.JsonParser
import net.uoneweb.android.gis.ui.location.Location
import net.uoneweb.android.til.ui.receipt.data.Receipt
import net.uoneweb.android.til.ui.receipt.data.ReceiptMetaData
import org.junit.Assert.assertEquals
import org.junit.Test

class ReceiptMetaDataTest {

    val receiptJson = """
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
        """.trimIndent()

    @Test
    fun testJsonFilename() {
        val receipt = Receipt(receiptJson)
        val filename = "filename"
        val receiptMetaData = ReceiptMetaData(receipt, null, null, filename)

        val actual = receiptMetaData.json()

        val actualJson = JsonParser.parseString(actual).asJsonObject
        val metaObj = actualJson?.get("meta")?.asJsonObject

        assertEquals("filename", metaObj?.get("filename")?.asString)

    }

    @Test
    fun testJsonLocation() {
        val receipt = Receipt(receiptJson)
        val location = Location(1.0, 2.0)
        val receiptMetaData = ReceiptMetaData(receipt, null, location, null)

        val actual = receiptMetaData.json()

        val actualJson = JsonParser.parseString(actual).asJsonObject
        val metaObj = actualJson?.get("meta")?.asJsonObject
        val actualLocation = metaObj?.get("location")?.asJsonObject

        assertEquals(1.0, actualLocation?.get("latitude")?.asDouble)
        assertEquals(2.0, actualLocation?.get("longitude")?.asDouble)

    }
}