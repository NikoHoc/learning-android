package com.dicoding.mystudentdata

import androidx.lifecycle.LiveData
import com.dicoding.mystudentdata.database.Student
import com.dicoding.mystudentdata.database.StudentAndUniversity
import com.dicoding.mystudentdata.database.StudentDao
import com.dicoding.mystudentdata.database.StudentWithCourse
import com.dicoding.mystudentdata.database.UniversityAndStudent
import com.dicoding.mystudentdata.helper.InitialDataSource

class StudentRepository(private val studentDao: StudentDao) {
    // one to one
    fun getAllStudent(): LiveData<List<Student>> = studentDao.getAllStudent()

    // many to one (many to student, one univ)
    fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>> = studentDao.getAllStudentAndUniversity()

    // one to many (1 univ, many student)
    fun getAllUniversityAndStudent(): LiveData<List<UniversityAndStudent>> = studentDao.getAllUniversityAndStudent()

    // many to many (student and course)
    fun getAllStudentWithCourse(): LiveData<List<StudentWithCourse>> = studentDao.getAllStudentWithCourse()

    suspend fun insertAllData() {
        studentDao.insertStudent(InitialDataSource.getStudents())
        studentDao.insertUniversity(InitialDataSource.getUniversities())
        studentDao.insertCourse(InitialDataSource.getCourses())
        studentDao.insertCourseStudentCrossRef(InitialDataSource.getCourseStudentRelation())

    }

}