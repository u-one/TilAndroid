package net.uoneweb.android.receipt.data

import android.util.Log
import com.google.gson.Gson

data class Receipt(
    val version: String = "v3",
    val store: Store = Store(),
    val receipt: ReceiptInfo = ReceiptInfo(),
    val items: List<Item> = emptyList(),
    val tax: Tax = Tax(),
    val total: Int = 0,
    val breakdown: Breakdown = Breakdown(),
    val payment: Payment = Payment(),
    val points: Points = Points(),
    val remarks: List<String> = emptyList(),
    val transaction: Transaction = Transaction(),
) {
    val json: String
        get() = Gson().toJson(this)


    fun title(): String {
        val date = receipt.date.replace("-", "")
        val time = receipt.time.replace(":", "")
        return if (store.branch.isNotBlank()) {
            "${date}_${time}_${total}_${version}_${store.name}_${store.branch}.json"
        } else {
            "${date}_${time}_${total}_${version}_${store.name}.json"
        }
    }

    fun store(): String {
        return if (store.branch.isNotBlank()) {
            "${store.name} ${store.branch}"
        } else {
            store.name
        }
    }

    fun total(): Int = total

    fun address(): String = store.address

    companion object {
        val Empty = Receipt()
        val Sample = Receipt.fromJson(
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

        val Sample2 = Receipt.fromJson(
            """
            {
              "store": {
                "name": "◯◯◯",
                "branch": "△△店",
                "tel": "012-3456-7890",
                "address": "東京都千代田区1-2-3",
                "postalCode": "123-4567",
                "website": "https://example.com",
                "email": "test@example.com"
              },
              "receipt": {
                "date": "2025-01-01",
                "time": "12:34"
              },
              "items": [
                {
                  "name": "むかし吉備団子15個 ※",
                  "price": 1728,
                  "quantity": 1
                },
                {
                  "name": "紙袋 小・縦小・中",
                  "price": 10,
                  "quantity": 2
                }
              ], 
              "total": 1738 
            }
            """.trimIndent(),
        )

        fun fromJson(jsonStr: String): Receipt {
            if (jsonStr.isEmpty()) {
                return Empty
            } else {
                return try {
                    Gson().fromJson(jsonStr, Receipt::class.java)
                } catch (e: Exception) {
                    Log.e("Receipt", "Error parsing json: $jsonStr", e)
                    Empty
                }
            }
        }
    }
}

data class Store(
    val name: String = "NA",
    val branch: String = "",
    val tel: String = "",
    val address: String = "",
    val postalCode: String = "",
    val website: String = "",
    val email: String = "",
    val openingHours: String = "",
)

data class ReceiptInfo(
    val type: String = "",
    val date: String = "",
    val time: String = "",
    val register: String = "",
    val cashier: String = "",
    val number: String = "",
    val issuer: String = "",
    val registrationNumber: String = "",
    val storeNumber: String = "",
    val responsiblePerson: String = "",
)

data class Item(
    val code: String = "",
    val name: String = "",
    val price: Int? = null,
    val quantity: Float? = null,
    val taxIncludedAmount: Int? = null,
    val taxAmount: Int? = null,
)

data class TaxRate(
    val target: Int = 0,
    val amount: Int = 0,
)

data class Tax(
    val rate8: TaxRate = TaxRate(),
    val rate10: TaxRate = TaxRate(),
)

data class Breakdown(
    val tax8: Int = 0,
    val tax10: Int = 0,
    val consumptionTax8: Int = 0,
    val consumptionTax10: Int = 0,
)

data class Card(
    val brand: String = "",
    val approval: String = "",
)

data class Payment(
    val method: String = "",
    val amount: Int = 0,
    val tendered: Int = 0,
    val change: Int = 0,
    val card: Card = Card(),
)

data class NormalPoints(
    val amount: Int = 0,
    val earned: Int = 0,
)

data class Points(
    val normal: NormalPoints = NormalPoints(),
    val earned: Int = 0,
)

data class Transaction(
    val type: String = "",
    val number: String = "",
)