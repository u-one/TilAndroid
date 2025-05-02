package net.uoneweb.android.til.ui.receipt.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import net.uoneweb.android.til.data.DatabaseProvider
import net.uoneweb.android.til.ui.receipt.data.ReceiptMetaData
import net.uoneweb.android.til.ui.receipt.dto.ReceiptMetaDataEntity

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

    suspend fun update(metadata: ReceiptMetaData) = withContext(Dispatchers.IO) {
        val entity = ReceiptMetaDataEntity.fromReceiptMetaData(metadata)
        dao.update(entity)
    }

    suspend fun delete(metadata: ReceiptMetaData) = withContext(Dispatchers.IO) {
        val entity = ReceiptMetaDataEntity.fromReceiptMetaData(metadata)
        dao.delete(entity)
    }
}