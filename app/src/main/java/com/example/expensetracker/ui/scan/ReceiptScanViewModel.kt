package com.example.expensetracker.ui.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.ocr.ReceiptOcrParser
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ReceiptScanViewModel @Inject constructor() : ViewModel() {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun processImage(image: InputImage, onResult: (Double?) -> Unit) {
        viewModelScope.launch {
            try {
                val result = recognizer.process(image).await()
                onResult(ReceiptOcrParser.extractAmount(result))
            } catch (e: Exception) {
                onResult(null)
            }
        }
    }
}