package net.uoneweb.android.receipt.dto

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import net.uoneweb.android.data.LocationConverter
import net.uoneweb.android.receipt.data.Receipt
import net.uoneweb.android.receipt.data.ReceiptMetaData

@Entity(
    tableName = "receipt_metadata",
    indices = [Index(value = ["receiptYearMonth"])],
)
data class ReceiptMetaDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String, // ReceiptのJSON文字列
    val location: String?, // LocationのJSON文字列
    val filename: String?,
    val receiptDate: String? = null, // "YYYY-MM-DD" 形式
    val receiptYearMonth: String? = null, // "YYYY-MM" 形式
) {
    companion object {
        fun fromReceiptMetaData(metadata: ReceiptMetaData): ReceiptMetaDataEntity {
            val date = metadata.content.receipt.date.takeIf { it.isNotEmpty() }
            val yearMonth = date?.take(7) // "YYYY-MM-DD" -> "YYYY-MM"
            return ReceiptMetaDataEntity(
                id = metadata.id ?: 0,
                content = metadata.content.json,
                location = metadata.location?.let { LocationConverter.toJson(it) },
                filename = metadata.filename,
                receiptDate = date,
                receiptYearMonth = yearMonth,
            )
        }

        fun toReceiptMetaData(entity: ReceiptMetaDataEntity): ReceiptMetaData {
            return ReceiptMetaData(
                id = entity.id,
                content = Receipt.fromJson(entity.content),
                location = entity.location?.let { LocationConverter.fromJson(it) },
                filename = entity.filename,
            )
        }
    }
}