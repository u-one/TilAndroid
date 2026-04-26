package net.uoneweb.android.receipt.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import net.uoneweb.android.data.DatabaseProvider
import net.uoneweb.android.receipt.data.ReceiptMetaData
import net.uoneweb.android.receipt.dto.ReceiptMetaDataEntity

class ReceiptMetaDataRepository(context: Context) {
    private val dao = DatabaseProvider.getDatabase(context).receiptMetaDataDao()

    suspend fun insert(metadata: ReceiptMetaData): Long = withContext(Dispatchers.IO) {
        val entity = ReceiptMetaDataEntity.fromReceiptMetaData(metadata)
        dao.insert(entity)
    }

    fun getById(id: Long): Flow<ReceiptMetaData?> {
        return dao.getById(id).map { it?.let { ReceiptMetaDataEntity.toReceiptMetaData(it) } }
    }

    fun getAll(): Flow<List<ReceiptMetaData>> {
        return dao.getAll().map { entities ->
            entities.map { ReceiptMetaDataEntity.toReceiptMetaData(it) }
        }
    }

    fun getYearMonthList(): Flow<List<String>> {
        return dao.getYearMonthList()
    }

    fun getByYearMonth(yearMonth: String): Flow<List<ReceiptMetaData>> {
        return dao.getByYearMonth(yearMonth).map { entities ->
            entities.map { ReceiptMetaDataEntity.toReceiptMetaData(it) }
                .sortedByDescending { it.content.receipt.date + "T" + it.content.receipt.time }
        }
    }

    fun getUnknownDate(): Flow<List<ReceiptMetaData>> {
        return dao.getUnknownDate().map { entities ->
            entities.map { ReceiptMetaDataEntity.toReceiptMetaData(it) }
        }
    }

    fun getByDate(date: String): Flow<List<ReceiptMetaData>> {
        return dao.getByDate(date).map { entities ->
            entities.map { ReceiptMetaDataEntity.toReceiptMetaData(it) }
                .sortedByDescending { it.content.receipt.time }
        }
    }

    fun getCountByYearMonth(yearMonth: String): Flow<Int> {
        return dao.getCountByYearMonth(yearMonth)
    }

    fun getCountUnknownDate(): Flow<Int> {
        return dao.getCountUnknownDate()
    }

    suspend fun update(id: Long, metadata: ReceiptMetaData) = withContext(Dispatchers.IO) {
        val entity = ReceiptMetaDataEntity.fromReceiptMetaData(metadata)
        dao.update(entity)
    }

    suspend fun delete(metadata: ReceiptMetaData) = withContext(Dispatchers.IO) {
        val entity = ReceiptMetaDataEntity.fromReceiptMetaData(metadata)
        dao.delete(entity)
    }

    suspend fun deleteAll() = withContext(Dispatchers.IO) {
        dao.deleteAll()
    }
}