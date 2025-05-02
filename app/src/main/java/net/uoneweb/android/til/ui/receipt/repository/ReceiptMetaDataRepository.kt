package net.uoneweb.android.til.ui.receipt.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
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

    suspend fun getById(id: Long): ReceiptMetaData? = withContext(Dispatchers.IO) {
        dao.getById(id)?.let { ReceiptMetaDataEntity.toReceiptMetaData(it) }
    }

    suspend fun getAll(): List<ReceiptMetaData> = withContext(Dispatchers.IO) {
        dao.getAll().map { ReceiptMetaDataEntity.toReceiptMetaData(it) }
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