package com.dicoding.mystudentdata.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RenameColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec

@Database(entities = [Student::class, University::class, Course::class, CourseStudentCrossRef::class],
    version = 3,
    autoMigrations = [
        /* Menambah field kolom baru */
        AutoMigration(from = 1, to = 2),
        /* Mengubah nama kolom graduate jadi isGraduate */
        AutoMigration(from = 2, to = 3, spec = StudentDatabase.MyAutoMigration::class),
    ],
    exportSchema = true)
abstract class StudentDatabase : RoomDatabase() {

    @RenameColumn(tableName = "Student", fromColumnName = "graduate", toColumnName = "isGraduate")
    class MyAutoMigration : AutoMigrationSpec

    abstract fun studentDao(): StudentDao

    companion object {
        @Volatile
        private var INSTANCE: StudentDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StudentDatabase {
            if (INSTANCE == null) {
                synchronized(StudentDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            StudentDatabase::class.java, "student_database")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE as StudentDatabase
        }

    }
}