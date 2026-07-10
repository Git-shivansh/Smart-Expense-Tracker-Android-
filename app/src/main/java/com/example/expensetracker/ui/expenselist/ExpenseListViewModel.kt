package com.example.expensetracker.ui.expenselist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.local.Expense
import com.example.expensetracker.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class ExpenseListUiState(
    val expenses: List<Expense> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String? = null
)

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val selectedCategory = MutableStateFlow<String?>(null)

    val uiState: StateFlow<ExpenseListUiState> = combine(
        searchQuery,
        selectedCategory,
        searchQuery.flatMapLatest { query ->
            selectedCategory.flatMapLatest { category ->
                repository.searchExpenses(query, category)
            }
        }
    ) { query, category, expenses ->
        ExpenseListUiState(expenses, query, category)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpenseListUiState())

    fun onSearchChange(query: String) {
        searchQuery.value = query
    }

    fun onCategoryFilter(category: String?) {
        selectedCategory.value = category
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch { repository.deleteExpense(expense) }
    }
}