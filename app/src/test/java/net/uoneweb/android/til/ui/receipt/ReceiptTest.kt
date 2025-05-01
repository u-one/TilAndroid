package net.uoneweb.android.til.ui.receipt

import net.uoneweb.android.til.ui.receipt.data.Receipt
import org.junit.Assert.assertEquals
import org.junit.Test

class ReceiptTest {

    @Test
    fun testTitle() {
        val json = """
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
        val receipt = Receipt(json)

        val actual = receipt.title()

        assertEquals("20250101_1234_5678_v2_store_branch.json", actual)
    }

    @Test
    fun testTitleNoBranch() {
        val json = """
            {
              "store": {
                "name": "store",
                "branch": ""
              },
              "receipt": {
                "date": "2025-01-01",
                "time": "12:34"
              },
              "total": 5678
            }
        """.trimIndent()
        val receipt = Receipt(json)

        val actual = receipt.title()

        assertEquals("20250101_1234_5678_v2_store.json", actual)
    }

    @Test
    fun testStore() {
        val json = """
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
        val receipt = Receipt(json)

        val actual = receipt.store()

        assertEquals("store branch", actual)
    }

    @Test
    fun testTotal() {
        val json = """
            {
              "store": {
                "name": "store",
                "branch": ""
              },
              "receipt": {
                "date": "2025-01-01",
                "time": "12:34"
              },
              "total": 5678
            }
        """.trimIndent()
        val receipt = Receipt(json)

        val actual = receipt.total()

        assertEquals(5678, actual)
    }

    @Test
    fun testAddress() {
        val json = """
            {
              "store": {
                "name": "store",
                "branch": "",
                "address": "address 1-2-3"
              },
              "receipt": {
                "date": "2025-01-01",
                "time": "12:34"
              },
              "total": 5678
            }
        """.trimIndent()
        val receipt = Receipt(json)

        val actual = receipt.address()

        assertEquals("address 1-2-3", actual)
    }
}