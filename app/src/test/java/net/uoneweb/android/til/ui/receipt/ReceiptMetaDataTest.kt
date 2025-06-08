package net.uoneweb.android.til.ui.receipt

import com.google.gson.JsonParser
import net.uoneweb.android.gis.ui.location.Location
import net.uoneweb.android.til.ui.receipt.data.Receipt
import net.uoneweb.android.til.ui.receipt.data.ReceiptMetaData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
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

    @Test
    fun testFromJsonWithFullData() {
        // 完全なデータを含むJSONを作成
        val json = """
            {
              "meta": {
                "version": "v3",
                "id": 12345,
                "location": {
                  "latitude": 35.6812,
                  "longitude": 139.7671
                },
                "filename": "test_receipt.json"
              },
              "receipt": $receiptJson
            }
        """.trimIndent()

        // テスト対象メソッドを実行
        val result = ReceiptMetaData.fromJson(json)

        // 検証
        assertEquals(12345L, result.id)
        assertNotNull(result.location)
        assertEquals(35.6812, result.location?.latitude?.value)
        assertEquals(139.7671, result.location?.longitude?.value)
        assertEquals("test_receipt.json", result.filename)
        assertEquals("store", result.content.store.name)
        assertEquals("branch", result.content.store.branch)
    }

    @Test
    fun testFromJsonWithPartialData() {
        // 一部のメタデータのみを含むJSONを作成
        val json = """
            {
              "meta": {
                "version": "v3",
                "filename": "partial_data.json"
              },
              "receipt": $receiptJson
            }
        """.trimIndent()

        // テスト対象メソッドを実行
        val result = ReceiptMetaData.fromJson(json)

        // 検証
        assertNull(result.id)
        assertNull(result.location)
        assertEquals("partial_data.json", result.filename)
        assertEquals("store", result.content.store.name)
    }

    @Test
    fun testFromJsonWithLegacyFormat() {
        // 古いフォーマット（receiptのみ）のJSONを作成
        val json = """
            {
              "receipt": $receiptJson
            }
        """.trimIndent()

        // テスト対象メソッドを実行
        val result = ReceiptMetaData.fromJson(json)

        // 検証
        assertNull(result.id)
        assertNull(result.location)
        assertNull(result.filename)
        assertEquals("store", result.content.store.name)
        assertEquals("branch", result.content.store.branch)
    }

    @Test()
    fun testFromJsonWithEmptyString() {
        // 空文字列でテスト
        val result = ReceiptMetaData.fromJson("")

        // 検証 - Emptyオブジェクトが返されることを確認
        assertEquals(ReceiptMetaData.Empty, result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testFromJsonWithInvalidJson() {
        // 不正なJSONでテスト
        ReceiptMetaData.fromJson("{\"unknown\": \"value\"}")
    }
}
