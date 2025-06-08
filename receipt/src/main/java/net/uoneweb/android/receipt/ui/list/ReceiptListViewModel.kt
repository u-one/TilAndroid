package net.uoneweb.android.receipt.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import net.uoneweb.android.receipt.data.ReceiptMetaData
import net.uoneweb.android.receipt.repository.ReceiptMetaDataRepository

/**
 * ViewModel for the Receipt List screen.
 */
class ReceiptListViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val repository = ReceiptMetaDataRepository(application)

    /**
     * UI state for the Receipt List screen.
     */
    val uiState: StateFlow<ReceiptListUiState> = repository.getAll()
        .map { receipts ->
            if (receipts.isEmpty()) {
                ReceiptListUiState.Empty
            } else {
                ReceiptListUiState.Success(receipts)
            }
        }
        .catch { e ->
            emit(ReceiptListUiState.Error(e.message ?: "Unknown error"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ReceiptListUiState.Loading,
        )

    /**
     * Deletes a receipt.
     */
    suspend fun deleteReceipt(receipt: ReceiptMetaData) {
        repository.delete(receipt)
    }
}

/**
 * UI state for the Receipt List screen.
 */
sealed class ReceiptListUiState {
    data object Loading : ReceiptListUiState()
    data object Empty : ReceiptListUiState()
    data class Success(val receipts: List<ReceiptMetaData>) : ReceiptListUiState()
    data class Error(val message: String) : ReceiptListUiState()
}
