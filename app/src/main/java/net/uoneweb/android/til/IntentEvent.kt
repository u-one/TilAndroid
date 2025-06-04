package net.uoneweb.android.til

sealed class IntentEvent {
    fun text(): String {
        return when (this) {
            is ReceiptV3 -> text
            is ReceiptV2 -> text
            is MultipleReceipts -> text
            is Unknown -> text
        }
    }

    data class MultipleReceipts(val text: String) : IntentEvent()
    data class ReceiptV3(val text: String) : IntentEvent()
    data class ReceiptV2(val text: String) : IntentEvent()
    data class Unknown(val text: String = "") : IntentEvent()
}


