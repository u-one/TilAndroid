package net.uoneweb.android.til.ui.receipt.data

import com.google.gson.annotations.SerializedName


data class ReceiptMappingInfo(
    @SerializedName("store_info_extracted")
    val storeInfoExtracted: List<ExtractedTagItem>,
    @SerializedName("existing_store_info")
    val existingStoreInfo: ExistingStoreInfo,
    val comparison: List<ComparisonItem>,
    @SerializedName("update_recommendation")
    val updateRecommendation: String,
)

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