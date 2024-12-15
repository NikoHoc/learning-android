package com.dicoding.mystudentdata.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface StudentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStudent(student: List<Student>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUniversity(university: List<University>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCourse(course: List<Course>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCourseStudentCrossRef(courseStudentCrossRef: List<CourseStudentCrossRef>)

    // normal
//    @Query("SELECT * from student")
//    fun getAllStudent(): LiveData<List<Student>>

    // raw query
//    @RawQuery(observedEntities = [Student::class])
//    fun getAllStudent(query: SupportSQLiteQuery): LiveData<List<Student>>

    // paging 2
    @RawQuery(observedEntities = [Student::class])
    fun getAllStudent(query: SupportSQLiteQuery): DataSource.Factory<Int, Student>

    @Transaction
    @Query("SELECT * from student")
    fun getAllStudentAndUniversity(): LiveData<List<StudentAndUniversity>>

    @Transaction
    @Query("SELECT * from university")
    fun getAllUniversityAndStudent(): LiveData<List<UniversityAndStudent>>

    @Transaction
    @Query("SELECT * from student")
    fun getAllStudentWithCourse(): LiveData<List<StudentWithCourse>>
}