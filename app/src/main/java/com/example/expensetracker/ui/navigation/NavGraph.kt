package com.example.expensetracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.ui.addexpense.AddEditExpenseScreen
import com.example.expensetracker.ui.charts.ChartsScreen
import com.example.expensetracker.ui.expenselist.ExpenseListScreen
import com.example.expensetracker.ui.scan.ReceiptScanScreen

object Routes {
    const val LIST = "list"
    const val ADD_EDIT = "add_edit?expenseId={expenseId}"
    const val SCAN = "scan"
    const val CHARTS = "charts"

    fun addEdit(expenseId: Long? = null) = "add_edit?expenseId=${expenseId ?: -1}"
}

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.LIST) {

        composable(Routes.LIST) {
            ExpenseListScreen(
                onAddClick = { navController.navigate(Routes.addEdit()) },
                onExpenseClick = { id -> navController.navigate(Routes.addEdit(id)) },
                onScanClick = { navController.navigate(Routes.SCAN) },
                onChartsClick = { navController.navigate(Routes.CHARTS) }
            )
        }

        composable(Routes.ADD_EDIT) { backStackEntry ->
            val expenseId = backStackEntry.arguments
                ?.getString("expenseId")?.toLongOrNull()?.takeIf { it != -1L }
            AddEditExpenseScreen(
                expenseId = expenseId,
                onDone = { navController.popBackStack() }
            )
        }

        composable(Routes.SCAN) {
            ReceiptScanScreen(
                onAmountDetected = { amount ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("scanned_amount", amount)
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.CHARTS) {
            ChartsScreen()
        }
    }
}