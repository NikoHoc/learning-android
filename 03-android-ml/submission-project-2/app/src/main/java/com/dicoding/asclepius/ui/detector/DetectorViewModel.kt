package com.dicoding.asclepius.ui.detector

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetectorViewModel : ViewModel() {
    private val _currentImageUri = MutableLiveData<Uri?>()
    val currentImageUri: LiveData<Uri?> get() = _currentImageUri

    fun setCurrentImageUri(uri: Uri?) {
        _currentImageUri.value = uri
    }
}