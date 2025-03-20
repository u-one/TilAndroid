package net.uoneweb.android.til.ui.receipt

import org.junit.Assert.assertEquals
import org.junit.Test


class ResponseParserTest {

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
        val responseParser = ResponseParser(text)
        val result = responseParser.json()
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