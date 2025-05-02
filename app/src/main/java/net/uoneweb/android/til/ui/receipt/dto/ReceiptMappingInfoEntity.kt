package net.uoneweb.android.til.ui.receipt.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "receipt_mapping_info")
data class ReceiptMappingInfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val key: String,
    val value: String,
)