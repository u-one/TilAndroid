package net.uoneweb.android.til.ui.receipt.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.uoneweb.android.til.ui.receipt.data.ReceiptMappingInfo

@Entity(tableName = "receipt_mapping_info")
data class ReceiptMappingInfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val receiptId: Long = 0,
    val content: String,
) {
    companion object {
        fun from(obj: ReceiptMappingInfo): ReceiptMappingInfoEntity {
            return ReceiptMappingInfoEntity(
                id = obj.id ?: 0,
                content = ReceiptMappingInfo.toJson(obj),
            )
        }

        fun toReceiptMetaData(entity: ReceiptMappingInfoEntity): ReceiptMappingInfo {
            return ReceiptMappingInfo.fromJson(entity.content)
        }
    }
}