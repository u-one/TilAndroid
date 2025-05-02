package net.uoneweb.android.til.ui.receipt.dto

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

    @Update
    suspend fun update(metadata: ReceiptMetaDataEntity)

    @Delete
    suspend fun delete(metadata: ReceiptMetaDataEntity)
}
