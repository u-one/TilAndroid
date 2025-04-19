package net.uoneweb.android.til.ui.receipt

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

        assertEquals("20250101_1234_5678_store_branch.json", actual)
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

        assertEquals("20250101_1234_5678_store.json", actual)
    }
}