package net.uoneweb.android.til.ui.receipt.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.uoneweb.android.til.data.LocationConverter
import net.uoneweb.android.til.ui.receipt.data.Receipt
import net.uoneweb.android.til.ui.receipt.data.ReceiptMetaData

@Entity(tableName = "receipt_metadata")
data class ReceiptMetaDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String, // ReceiptのJSON文字列
    val location: String?, // LocationのJSON文字列
    val filename: String?,
) {
    companion object {
        fun fromReceiptMetaData(metadata: ReceiptMetaData): ReceiptMetaDataEntity {
            return ReceiptMetaDataEntity(
                id = metadata.id ?: 0,
                content = metadata.content.json,
                location = metadata.location?.let { LocationConverter.toJson(it) },
                filename = metadata.filename,
            )
        }

        fun toReceiptMetaData(entity: ReceiptMetaDataEntity): ReceiptMetaData {
            return ReceiptMetaData(
                id = entity.id,
                content = Receipt(entity.content),
                location = entity.location?.let { LocationConverter.fromJson(it) },
                filename = entity.filename,
            )
        }
    }
}