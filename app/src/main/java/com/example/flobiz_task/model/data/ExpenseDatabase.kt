package com.example.flobiz_task.model.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Expense::class], version = 2,exportSchema = false)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao



    companion object {
        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add the new column with a default value
                database.execSQL("ALTER TABLE expense ADD COLUMN markedForDeletion INTEGER NOT NULL DEFAULT 0")
            }
        }


        fun getDatabase(context: Context): ExpenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_database"
                )
                    .addMigrations(MIGRATION_1_2) // Add the migration here
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }




}