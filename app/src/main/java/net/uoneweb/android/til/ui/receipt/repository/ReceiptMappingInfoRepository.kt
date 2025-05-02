package net.uoneweb.android.til.ui.receipt.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.uoneweb.android.til.data.DatabaseProvider
import net.uoneweb.android.til.ui.receipt.dto.ReceiptMappingInfoEntity

class ReceiptMappingInfoRepository(context: Context) {
    private val dao = DatabaseProvider.getDatabase(context).receiptMappingInfoDao()

    suspend fun insert(mappingInfo: ReceiptMappingInfoEntity): Long = withContext(Dispatchers.IO) {
        dao.insert(mappingInfo)
    }

    suspend fun getById(id: Long): ReceiptMappingInfoEntity? = withContext(Dispatchers.IO) {
        dao.getById(id)
    }

    suspend fun getAll(): List<ReceiptMappingInfoEntity> = withContext(Dispatchers.IO) {
        dao.getAll()
    }

    suspend fun update(mappingInfo: ReceiptMappingInfoEntity) = withContext(Dispatchers.IO) {
        dao.update(mappingInfo)
    }

    suspend fun delete(mappingInfo: ReceiptMappingInfoEntity) = withContext(Dispatchers.IO) {
        dao.delete(mappingInfo)
    }
}
