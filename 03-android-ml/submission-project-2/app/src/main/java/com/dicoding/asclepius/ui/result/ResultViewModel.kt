package com.dicoding.asclepius.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.DataRepository
import kotlinx.coroutines.launch

class ResultViewModel(private val dataRepository: DataRepository) : ViewModel() {
    fun saveResult(imageData: String?, result: String?) {
        viewModelScope.launch {
            dataRepository.insertToHistory(imageData, result)
        }
    }

    fun isResultSaved(imageUri: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isSaved = dataRepository.isResultSaved(imageUri)
            callback(isSaved)
        }
    }
}