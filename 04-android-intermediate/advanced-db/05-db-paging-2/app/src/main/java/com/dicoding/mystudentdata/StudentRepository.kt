package com.dicoding.mystudentdata

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dicoding.mystudentdata.database.*
import com.dicoding.mystudentdata.helper.SortType
import com.dicoding.mystudentdata.helper.SortUtils

class StudentRepository(private val studentDao: StudentDao) {
    // paging 2
    fun getAllStudent(sortType: SortType): LiveData<PagedList<Student>> {
        val query = SortUtils.getSortedQuery(sortType)
        val student = studentDao.getAllStudent(query)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(30)
            .setPageSize(10)
            .build()
        return LivePagedListBuilder(student, config).build()
    }
    // raw query ascending descending random
//    fun getAllStudent(sortType: SortType): LiveData<List<Student>> {
//        val query = SortUtils.getSortedQuery(sortType)
//        return studentDao.getAllStudent(query)
//    }
//    fun getAllStudent(): LiveData<List<Student>> = studentDao.getAllStudent()
    fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>> = studentDao.getAllStudentAndUniversity()
    fun getAllUniversityAndStudent(): LiveData<List<UniversityAndStudent>> = studentDao.getAllUniversityAndStudent()
    fun getAllStudentWithCourse(): LiveData<List<StudentWithCourse>> = studentDao.getAllStudentWithCourse()
}