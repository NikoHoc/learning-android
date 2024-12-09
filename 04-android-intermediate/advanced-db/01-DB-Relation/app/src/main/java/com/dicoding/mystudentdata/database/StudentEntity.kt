package com.dicoding.mystudentdata.database

import androidx.room.*

@Entity
data class Student(
    @PrimaryKey
    val studentId: Int,
    val name: String,
    val univId: Int,
)

@Entity
data class University(
    @PrimaryKey
    val universityId: Int,
    val name: String,
)

@Entity
data class Course(
    @PrimaryKey
    val courseId: Int,
    val name: String,
)

// many to one (many to student, one univ)
data class StudentAndUniversity(
    @Embedded
    val student: Student,
    @Relation(
        parentColumn = "univId",
        entityColumn = "universityId"
    )
    val university: University? = null
)

// one to many (1 univ, many student)
data class UniversityAndStudent(
    @Embedded
    val university: University,
    @Relation(
        parentColumn = "universityId",
        entityColumn = "univId"
    )
    val student: List<Student>
)

// many to many (student many course, course many student)
@Entity(primaryKeys = ["sId", "cId"])
data class CourseStudentCrossRef(
    val sId: Int,
    @ColumnInfo(index = true)
    val cId: Int,
)
data class StudentWithCourse(
//    @Embedded
//    val student: Student,

    // student with university and course
    @Embedded
    val studentAndUniversity: StudentAndUniversity,
    @Relation(
        parentColumn = "studentId",
        entity = Course::class,
        entityColumn = "courseId",
        associateBy = Junction(
            value = CourseStudentCrossRef::class,
            parentColumn = "sId",
            entityColumn = "cId"
        )
    )
    val course: List<Course>
)
