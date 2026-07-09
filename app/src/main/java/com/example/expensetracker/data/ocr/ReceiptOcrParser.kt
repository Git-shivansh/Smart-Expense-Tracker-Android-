package com.example.expensetracker.data.ocr

import com.google.mlkit.vision.text.Text
import java.util.regex.Pattern

/**
 * Very pragmatic amount extractor: scans OCR text for currency-looking
 * numbers and picks the largest one (receipts usually have the total
 * as the biggest number on the page).
 */
object ReceiptOcrParser {

    private val AMOUNT_PATTERN = Pattern.compile("""(?:₹|Rs\.?|INR)?\s?(\d{1,6}(?:[.,]\d{2})?)""")

    fun extractAmount(visionText: Text): Double? {
        val candidates = mutableListOf<Double>()
        val matcher = AMOUNT_PATTERN.matcher(visionText.text)
        while (matcher.find()) {
            matcher.group(1)?.replace(",", "")?.toDoubleOrNull()?.let { candidates.add(it) }
        }
        return candidates.maxOrNull()
    }
}