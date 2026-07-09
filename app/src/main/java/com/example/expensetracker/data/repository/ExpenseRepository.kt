package com.example.expensetracker.data.repository

import com.example.expensetracker.data.local.CategoryTotal
import com.example.expensetracker.data.local.Expense
import com.example.expensetracker.data.local.ExpenseDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepository @Inject constructor(
    private val dao: ExpenseDao
) {
    fun getAllExpenses(): Flow<List<Expense>> = dao.getAllExpenses()

    fun searchExpenses(query: String, category: String?): Flow<List<Expense>> =
        dao.searchExpenses(query, category)

    fun getCategoryTotals(start: Long, end: Long): Flow<List<CategoryTotal>> =
        dao.getCategoryTotals(start, end)

    suspend fun getExpenseById(id: Long): Expense? = dao.getExpenseById(id)

    suspend fun addExpense(expense: Expense): Long = dao.insert(expense)

    suspend fun updateExpense(expense: Expense) = dao.update(expense)

    suspend fun deleteExpense(expense: Expense) = dao.delete(expense)
}