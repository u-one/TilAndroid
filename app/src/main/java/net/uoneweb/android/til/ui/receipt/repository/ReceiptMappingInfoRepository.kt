package net.uoneweb.android.til.ui.receipt.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import net.uoneweb.android.til.data.DatabaseProvider
import net.uoneweb.android.til.ui.receipt.data.ReceiptMappingInfo
import net.uoneweb.android.til.ui.receipt.dto.ReceiptMappingInfoEntity

class ReceiptMappingInfoRepository(context: Context) {
    private val dao = DatabaseProvider.getDatabase(context).receiptMappingInfoDao()

    suspend fun insert(obj: ReceiptMappingInfo): Long = withContext(Dispatchers.IO) {
        val entity = ReceiptMappingInfoEntity.from(obj)
        dao.insert(entity)
    }

    fun getById(id: Long): Flow<ReceiptMappingInfo?> {
        return dao.getById(id).map {
            it?.let {
                ReceiptMappingInfoEntity.toReceiptMetaData(it)
            }
        }
    }

    fun getByReceiptId(receiptId: Long): Flow<List<ReceiptMappingInfo>> {
        if (receiptId == 0L) {
            return flowOf(emptyList())
        }
        return dao.getByReceiptId(receiptId).map { flow ->
            flow.map { ReceiptMappingInfoEntity.toReceiptMetaData(it) }
        }
    }

    fun getAll(): Flow<List<ReceiptMappingInfo>> {
        return dao.getAll().map { flow ->
            flow.map { ReceiptMappingInfoEntity.toReceiptMetaData(it) }
        }
    }

    suspend fun update(obj: ReceiptMappingInfo) = withContext(Dispatchers.IO) {
        dao.update(ReceiptMappingInfoEntity.from(obj))
    }

    suspend fun delete(obj: ReceiptMappingInfo) = withContext(Dispatchers.IO) {
        dao.delete(ReceiptMappingInfoEntity.from(obj))
    }
}
