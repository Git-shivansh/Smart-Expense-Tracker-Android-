package com.example.expensetracker.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expenses ORDER BY dateMillis DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("""
        SELECT * FROM expenses 
        WHERE (:category IS NULL OR category = :category)
        AND (title LIKE '%' || :query || '%' OR note LIKE '%' || :query || '%')
        ORDER BY dateMillis DESC
    """)
    fun searchExpenses(query: String, category: String?): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE id = :id")
    suspend fun getExpenseById(id: Long): Expense?

    @Query("""
        SELECT category AS category, SUM(amount) AS total 
        FROM expenses 
        WHERE dateMillis BETWEEN :start AND :end
        GROUP BY category
    """)
    fun getCategoryTotals(start: Long, end: Long): Flow<List<CategoryTotal>>

    @Insert
    suspend fun insert(expense: Expense): Long

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)
}

data class CategoryTotal(
    val category: String,
    val total: Double
)