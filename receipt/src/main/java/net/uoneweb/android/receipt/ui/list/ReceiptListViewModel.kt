package net.uoneweb.android.receipt.ui.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import net.uoneweb.android.receipt.UNKNOWN_DATE_KEY
import net.uoneweb.android.receipt.currentYearMonth
import net.uoneweb.android.receipt.data.ReceiptMetaData
import net.uoneweb.android.receipt.repository.ReceiptMetaDataRepository

@OptIn(ExperimentalCoroutinesApi::class)
class ReceiptListViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val repository = ReceiptMetaDataRepository(application)

    private val _selectedYearMonth = MutableStateFlow(currentYearMonth())
    val selectedYearMonth: StateFlow<String> = _selectedYearMonth

    private val yearMonthListFlow = repository.getYearMonthList()
    private val unknownCountFlow = repository.getCountUnknownDate()

    private val selectedReceiptsFlow = _selectedYearMonth.flatMapLatest { yearMonth ->
        if (yearMonth == UNKNOWN_DATE_KEY) {
            repository.getUnknownDate()
        } else {
            repository.getByYearMonth(yearMonth)
        }
    }

    val uiState: StateFlow<ReceiptListUiState> = combine(
        yearMonthListFlow,
        unknownCountFlow,
        _selectedYearMonth,
        selectedReceiptsFlow,
    ) { yearMonthList, unknownCount, selectedYearMonth, receipts ->
        if (yearMonthList.isEmpty() && unknownCount == 0) {
            ReceiptListUiState.Empty
        } else {
            val options = yearMonthList.toMutableList()
            if (unknownCount > 0) {
                options.add(UNKNOWN_DATE_KEY)
            }
            ReceiptListUiState.Success(
                yearMonthOptions = options,
                selectedYearMonth = selectedYearMonth,
                receipts = receipts,
                total = receipts.sumOf { it.content.total },
            )
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

    fun selectYearMonth(yearMonth: String) {
        _selectedYearMonth.value = yearMonth
    }

    suspend fun deleteReceipt(receipt: ReceiptMetaData) {
        repository.delete(receipt)
    }
}

sealed class ReceiptListUiState {
    data object Loading : ReceiptListUiState()
    data object Empty : ReceiptListUiState()
    data class Success(
        val yearMonthOptions: List<String>,
        val selectedYearMonth: String,
        val receipts: List<ReceiptMetaData>,
        val total: Int,
    ) : ReceiptListUiState()
    data class Error(val message: String) : ReceiptListUiState()
}
