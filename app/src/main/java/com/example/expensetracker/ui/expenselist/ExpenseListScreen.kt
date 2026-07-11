package com.example.expensetracker.ui.expenselist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.expensetracker.data.local.Expense
import androidx.compose.foundation.clickable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    onAddClick: () -> Unit,
    onExpenseClick: (Long) -> Unit,
    onScanClick: () -> Unit,
    onChartsClick: () -> Unit,
    viewModel: ExpenseListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expenses") },
                actions = {
                    IconButton(onClick = onChartsClick) { Text("📊") }
                    IconButton(onClick = onScanClick) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "Scan receipt")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add expense")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = viewModel::onSearchChange,
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth().padding(12.dp)
            )

            LazyColumn {
                items(state.expenses, key = { it.id }) { expense ->
                    ExpenseRow(
                        expense = expense,
                        onClick = { onExpenseClick(expense.id) },
                        onDelete = { viewModel.deleteExpense(expense) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpenseRow(expense: Expense, onClick: () -> Unit, onDelete: () -> Unit) {
    ListItem(
        headlineContent = { Text(expense.title) },
        supportingContent = { Text(expense.category) },
        trailingContent = {
            Row {
                Text("₹${"%.2f".format(expense.amount)}")
                IconButton(onClick = onDelete) { Text("🗑") }
            }
        },
        modifier = Modifier.clickable(onClick = onClick)
    )
}