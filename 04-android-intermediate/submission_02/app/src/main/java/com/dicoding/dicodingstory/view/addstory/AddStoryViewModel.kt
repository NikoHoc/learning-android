package com.dicoding.dicodingstory.view.addstory

import androidx.lifecycle.ViewModel
import com.dicoding.dicodingstory.data.DataRepository
import java.io.File

class AddStoryViewModel(private val repository: DataRepository) : ViewModel() {
    fun uploadStory(
        file: File,
        description: String,
        lat: Float? = null,
        lon: Float? = null
    ) = repository.uploadStory(file, description, lat, lon)
}