package net.uoneweb.android.receipt.data

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName


data class ReceiptMappingInfo(
    @SerializedName("store_info_extracted")
    val storeInfoExtracted: List<ExtractedTagItem>,
    @SerializedName("existing_store_info")
    val existingStoreInfo: ExistingStoreInfo,
    val comparison: List<ComparisonItem>,
    @SerializedName("update_recommendation")
    val updateRecommendation: String,
    val id: Long? = null,
    val receiptId: Long? = null,
) {
    companion object {
        fun fromJson(jsonString: String): ReceiptMappingInfo {
            val gson = Gson()
            try {
                return gson.fromJson(jsonString, ReceiptMappingInfo::class.java) ?: Empty
            } catch (e: JsonSyntaxException) {
                return ReceiptMappingInfo(listOf(), ExistingStoreInfo(listOf(), ""), listOf(), e.message ?: "invalid json")
            }
        }

        fun toJson(obj: ReceiptMappingInfo): String {
            val gson = Gson()
            return gson.toJson(obj)
        }

        val Empty = ReceiptMappingInfo(
            storeInfoExtracted = listOf(),
            existingStoreInfo = ExistingStoreInfo(listOf(), ""),
            comparison = listOf(),
            updateRecommendation = "",
        )
    }
}

data class ExtractedTagItem(
    val key: String,
    val value: String,
    @SerializedName("based_on")
    val basedOn: Certainty = Certainty.Unknown,
    val reason: String,
    val comment: String,
)

enum class Certainty {
    Unknown,

    @SerializedName("fact")
    Fact,

    @SerializedName("inference")
    Inference,
}

data class ExistingStoreInfo(
    val tags: List<ExistingStoreTagItem>,
    val comment: String,
)

data class ExistingStoreTagItem(
    val key: String,
    val value: String,
    val comment: String,
)

data class ComparisonItem(
    val key: String,
    @SerializedName("existing_value")
    val existingValue: String,
    @SerializedName("new_value")
    val newValue: String,
    val difference: Difference = Difference.Unknown,
    val comment: String,
)

enum class Difference {
    Unknown,

    @SerializedName("same")
    Same,

    @SerializedName("different")
    Different,

    @SerializedName("new")
    New,
}