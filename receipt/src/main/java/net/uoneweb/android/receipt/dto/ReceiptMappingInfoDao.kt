package net.uoneweb.android.receipt.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptMappingInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mappingInfo: ReceiptMappingInfoEntity): Long

    @Query("SELECT * FROM receipt_mapping_info WHERE id = :id")
    fun getById(id: Long): Flow<ReceiptMappingInfoEntity?>

    @Query("SELECT * FROM receipt_mapping_info")
    fun getAll(): Flow<List<ReceiptMappingInfoEntity>>

    @Query("SELECT * FROM receipt_mapping_info WHERE receiptId = :receiptId")
    fun getByReceiptId(receiptId: Long): Flow<List<ReceiptMappingInfoEntity>>

    @Update
    suspend fun update(mappingInfo: ReceiptMappingInfoEntity)

    @Delete
    suspend fun delete(mappingInfo: ReceiptMappingInfoEntity)
}

