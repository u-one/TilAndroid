package net.uoneweb.android.receipt.dto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptMetaDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(metadata: ReceiptMetaDataEntity): Long

    @Query("SELECT * FROM receipt_metadata WHERE id = :id")
    fun getById(id: Long): Flow<ReceiptMetaDataEntity?>

    @Query("SELECT * FROM receipt_metadata")
    fun getAll(): Flow<List<ReceiptMetaDataEntity>>

    @Query("SELECT DISTINCT receiptYearMonth FROM receipt_metadata WHERE receiptYearMonth IS NOT NULL ORDER BY receiptYearMonth DESC")
    fun getYearMonthList(): Flow<List<String>>

    @Query("SELECT * FROM receipt_metadata WHERE receiptYearMonth = :yearMonth ORDER BY receiptDate DESC")
    fun getByYearMonth(yearMonth: String): Flow<List<ReceiptMetaDataEntity>>

    @Query("SELECT * FROM receipt_metadata WHERE receiptYearMonth IS NULL ORDER BY id DESC")
    fun getUnknownDate(): Flow<List<ReceiptMetaDataEntity>>

    @Query("SELECT COUNT(*) FROM receipt_metadata WHERE receiptYearMonth = :yearMonth")
    fun getCountByYearMonth(yearMonth: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM receipt_metadata WHERE receiptYearMonth IS NULL")
    fun getCountUnknownDate(): Flow<Int>

    @Update
    suspend fun update(metadata: ReceiptMetaDataEntity)

    @Delete
    suspend fun delete(metadata: ReceiptMetaDataEntity)

    @Query("DELETE FROM receipt_metadata")
    suspend fun deleteAll()
}
