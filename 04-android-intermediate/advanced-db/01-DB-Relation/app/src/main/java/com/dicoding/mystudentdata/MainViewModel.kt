package com.dicoding.mystudentdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dicoding.mystudentdata.database.Student
import com.dicoding.mystudentdata.database.StudentAndUniversity
import com.dicoding.mystudentdata.database.StudentWithCourse
import com.dicoding.mystudentdata.database.UniversityAndStudent
import kotlinx.coroutines.launch

class MainViewModel(private val studentRepository: StudentRepository) : ViewModel() {

    init {
        insertAllData()
    }

    // one to one
    fun getAllStudent(): LiveData<List<Student>> = studentRepository.getAllStudent()

    // many to one (many to student, one univ)
    fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>> = studentRepository.getAllStudentAndUniversity()

    // one to many (1 univ, many student)
    fun getAllUniversityAndStudent(): LiveData<List<UniversityAndStudent>> = studentRepository.getAllUniversityAndStudent()

    // many to many (student course)
    fun getAllStudentWithCourse(): LiveData<List<StudentWithCourse>> = studentRepository.getAllStudentWithCourse()

    private fun insertAllData() = viewModelScope.launch {
        studentRepository.insertAllData()
    }
}

class ViewModelFactory(private val repository: StudentRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}