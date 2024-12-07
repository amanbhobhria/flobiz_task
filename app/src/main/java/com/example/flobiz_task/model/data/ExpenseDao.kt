package com.example.flobiz_task.model.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ExpenseDao {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllExpenses(expenses: List<Expense>)

    @Query("SELECT * FROM expense")
    suspend fun getAllExpenses(): List<Expense>

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("DELETE FROM expense")
    suspend fun deleteAllExpenses()

    @Query ("DELETE FROM expense WHERE id = :expenseId")
    suspend fun deleteExpenseById(expenseId: String)

    @Query("SELECT * FROM expense WHERE synced = 0")
    suspend fun getUnsyncedExpenses(): List<Expense>


    @Update
    suspend fun updateExpense(expense: Expense)




    @Query("SELECT * FROM expense WHERE markedForDeletion = 1")
    suspend fun getMarkedForDeletionExpenses(): List<Expense>

    @Query("UPDATE expense SET markedForDeletion = 1 WHERE id = :expenseId")
    suspend fun markExpenseForDeletion(expenseId: String)



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(expense: Expense)

    @Query("SELECT * FROM expense WHERE id = :expenseId")
    suspend fun getExpenseById(expenseId: String): Expense?









}
