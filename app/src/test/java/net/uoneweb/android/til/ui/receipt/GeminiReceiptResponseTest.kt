package net.uoneweb.android.til.ui.receipt

import net.uoneweb.android.receipt.webapi.GeminiReceiptResponse
import org.junit.Assert.assertEquals
import org.junit.Test


class GeminiReceiptResponseTest {

    val text = """
    ```json
    {
      "store": {
        "name": "string"
      },
      "items": [
        {
          "name": "string",
          "price": "number"
        }
      ]
    }
    ```
    """.trimIndent()

    @Test
    fun testJson() {
        val geminiReceiptResponse = GeminiReceiptResponse(text)
        val result = geminiReceiptResponse.json()
        assertEquals(
            """
            {
              "store": {
                "name": "string"
              },
              "items": [
                {
                  "name": "string",
                  "price": "number"
                }
              ]
            }
            """.trimIndent(),
            result,
        )
    }
}