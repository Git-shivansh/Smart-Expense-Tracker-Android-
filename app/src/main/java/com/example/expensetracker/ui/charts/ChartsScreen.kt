package com.example.expensetracker.ui.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

private val PALETTE = listOf(
    Color(0xFF6750A4), Color(0xFF03DAC6), Color(0xFFFFB300),
    Color(0xFFEF5350), Color(0xFF42A5F5), Color(0xFF8BC34A)
)

@Composable
fun ChartsScreen(viewModel: ChartsViewModel = hiltViewModel()) {
    val totals by viewModel.categoryTotals.collectAsState()
    val grandTotal = totals.sumOf { it.total }.coerceAtLeast(0.01)

    Column(modifier = Modifier.padding(16.dp)) {
        Text("This Month by Category", style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        Canvas(modifier = Modifier.size(220.dp)) {
            var startAngle = -90f
            totals.forEachIndexed { index, item ->
                val sweep = (item.total / grandTotal * 360f).toFloat()
                drawArc(
                    color = PALETTE[index % PALETTE.size],
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true
                )
                startAngle += sweep
            }
        }

        Spacer(Modifier.height(16.dp))

        totals.forEachIndexed { index, item ->
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Canvas(modifier = Modifier.size(14.dp)) {
                    drawCircle(color = PALETTE[index % PALETTE.size])
                }
                Spacer(Modifier.width(8.dp))
                Text("${item.category}: ₹${"%.2f".format(item.total)}")
            }
        }
    }
}