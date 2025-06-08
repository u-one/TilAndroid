package net.uoneweb.android.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.uoneweb.android.receipt.dto.ReceiptMappingInfoDao
import net.uoneweb.android.receipt.dto.ReceiptMappingInfoEntity
import net.uoneweb.android.receipt.dto.ReceiptMetaDataDao
import net.uoneweb.android.receipt.dto.ReceiptMetaDataEntity

@Database(entities = [ReceiptMetaDataEntity::class, ReceiptMappingInfoEntity::class], version = 1)
@TypeConverters(LocationConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun receiptMetaDataDao(): ReceiptMetaDataDao
    abstract fun receiptMappingInfoDao(): ReceiptMappingInfoDao
}