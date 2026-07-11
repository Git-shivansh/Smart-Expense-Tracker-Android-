package com.example.expensetracker.ui.addexpense

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.local.Expense
import com.example.expensetracker.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditUiState(
    val id: Long? = null,
    val title: String = "",
    val amount: String = "",
    val category: String = "General",
    val note: String = "",
    val saved: Boolean = false
)

val CATEGORIES = listOf("Food", "Travel", "Shopping", "Bills", "Health", "General")

@HiltViewModel
class AddEditExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditUiState())
    val uiState: StateFlow<AddEditUiState> = _uiState.asStateFlow()

    fun loadExpense(id: Long) {
        viewModelScope.launch {
            repository.getExpenseById(id)?.let { expense ->
                _uiState.value = AddEditUiState(
                    id = expense.id,
                    title = expense.title,
                    amount = expense.amount.toString(),
                    category = expense.category,
                    note = expense.note
                )
            }
        }
    }

    fun onTitleChange(v: String) { _uiState.value = _uiState.value.copy(title = v) }
    fun onAmountChange(v: String) { _uiState.value = _uiState.value.copy(amount = v) }
    fun onCategoryChange(v: String) { _uiState.value = _uiState.value.copy(category = v) }
    fun onNoteChange(v: String) { _uiState.value = _uiState.value.copy(note = v) }

    fun save() {
        val state = _uiState.value
        val amountValue = state.amount.toDoubleOrNull() ?: return
        viewModelScope.launch {
            val expense = Expense(
                id = state.id ?: 0,
                title = state.title,
                amount = amountValue,
                category = state.category,
                dateMillis = System.currentTimeMillis(),
                note = state.note
            )
            if (state.id == null) repository.addExpense(expense)
            else repository.updateExpense(expense)
            _uiState.value = state.copy(saved = true)
        }
    }
}