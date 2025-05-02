package net.uoneweb.android.til.ui.receipt.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ReceiptMappingInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mappingInfo: ReceiptMappingInfoEntity): Long

    @Query("SELECT * FROM receipt_mapping_info WHERE id = :id")
    suspend fun getById(id: Long): ReceiptMappingInfoEntity?

    @Query("SELECT * FROM receipt_mapping_info")
    suspend fun getAll(): List<ReceiptMappingInfoEntity>

    @Update
    suspend fun update(mappingInfo: ReceiptMappingInfoEntity)

    @Delete
    suspend fun delete(mappingInfo: ReceiptMappingInfoEntity)
}

