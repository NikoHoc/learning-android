package com.dicoding.mystudentdata.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStudent(student: List<Student>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUniversity(university: List<University>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCourse(course: List<Course>)

    // one to one
    @Query("SELECT * from student")
    fun getAllStudent(): LiveData<List<Student>>

    // many to one (many to student, one univ)
    @Transaction
    @Query("SELECT * from student")
    fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>>

    // one to many (1 univ, many student)
    @Transaction
    @Query("SELECT * from university")
    fun getAllUniversityAndStudent(): LiveData<List<UniversityAndStudent>>

    // many to many (student and course)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCourseStudentCrossRef(courseStudentCrossRef: List<CourseStudentCrossRef>)

    @Transaction
    @Query("SELECT * from student")
    fun getAllStudentWithCourse(): LiveData<List<StudentWithCourse>>
}