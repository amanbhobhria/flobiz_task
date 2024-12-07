package com.example.flobiz_task.model.repository

import android.util.Log
import com.example.flobiz_task.model.data.Expense
import com.example.flobiz_task.model.data.ExpenseDao
import com.example.flobiz_task.utility.Utility
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ExpenseRepository @Inject constructor(

    private val databaseReference: DatabaseReference,
    private val expenseDao: ExpenseDao,
    private val utility: Utility,
) {




    suspend fun getExpenses(): List<Expense> {
        return if (utility.isConnectedToInternet()) {
            fetchExpensesFromFirebase()
        } else {
            fetchExpensesFromCache()
        }
    }





    suspend fun uploadExpense(expense: Expense): Boolean {
        return if (utility.isConnectedToInternet()) {
            try {
                val key = databaseReference.push().key ?: throw Exception("Unable to generate key")
                expense.id = key
                databaseReference.child(key).setValue(expense).await()
                expenseDao.insertExpense(expense.copy(synced = true))
                Log.d("ExpenseRepository", "Online expense saved: $expense")
                true
            } catch (e: Exception) {
                throw Exception("Failed to upload expense: ${e.message}")
            }
        } else {
            // Save offline if no connection
            saveExpenseToLocal(expense)
            Log.d("ExpenseRepository", "Offline expense saved: $expense")
            true // Indicates data is saved locally, not uploaded
        }
    }


    suspend fun saveExpenseToLocal(expense: Expense) {
        expenseDao.insertExpense(expense.copy(synced = false))
    }

    suspend fun syncOfflineExpenses() {
        if (utility.isConnectedToInternet()) {
            try {
                // Sync unsynced expenses (new and updated)
                val unsyncedExpenses = expenseDao.getUnsyncedExpenses()
                unsyncedExpenses.forEach { expense ->
                    try {
                        val key = if (expense.id.isEmpty()) databaseReference.push().key else expense.id
                        expense.id = key ?: throw Exception("Unable to generate key")
                        databaseReference.child(key).setValue(expense).await()
                        expenseDao.updateExpense(expense.copy(synced = true)) // Mark as synced
                        Log.d("ExpenseRepository", "Synced expense: $expense")
                    } catch (e: Exception) {
                        Log.e("ExpenseRepository", "Error syncing expense: ${e.message}", e)
                    }
                }

                // Sync deleted expenses
                val deletedExpenses = expenseDao.getMarkedForDeletionExpenses()
                deletedExpenses.forEach { expense ->
                    try {
                        databaseReference.child(expense.id).removeValue().await()
                        expenseDao.deleteExpense(expense) // Remove from local DB
                        Log.d("ExpenseRepository", "Synced deleted expense: $expense")
                    } catch (e: Exception) {
                        Log.e("ExpenseRepository", "Error syncing deleted expense: ${e.message}", e)
                    }
                }
            } catch (e: Exception) {
                Log.e("ExpenseRepository", "Error during sync: ${e.message}", e)
            }
        }
    }



    // Fetch expenses from Firebase and cache them in Room
    suspend fun fetchExpensesFromFirebase(): List<Expense> {
        return try {
            val snapshot = databaseReference.get().await()
            val expenseList = snapshot.children.mapNotNull { it.getValue(Expense::class.java) }


            expenseDao.deleteAllExpenses()
            expenseDao.insertAllExpenses(expenseList)
            expenseList


        } catch (e: Exception) {
            Log.e("ExpenseRepository", "Error fetching expenses: ${e.message}", e)

            expenseDao.getAllExpenses() // Fallback to local cache
        }
    }


    // Fetch expenses from Room only (offline mode)
    suspend fun fetchExpensesFromCache(): List<Expense> {
        return expenseDao.getAllExpenses()
    }


    // Update expense in Firebase and cache
    suspend fun updateExpense(expense: Expense): Boolean {
        return try {
            // Update locally with "synced = false"
            expenseDao.updateExpense(expense.copy(synced = false))

            // Sync with Firebase if connected
            if (utility.isConnectedToInternet()) {
                val key = expense.id
                databaseReference.child(key).setValue(expense).await()
                expenseDao.updateExpense(expense.copy(synced = true)) // Mark as synced
            }

            true // Update was successful (locally at minimum)
        } catch (e: Exception) {
            Log.e("ExpenseRepository", "Failed to update expense: ${e.message}")
            false
        }
    }







    suspend fun deleteExpense(expenseId: String): Boolean {
        return try {
            // Mark the expense for deletion locally (soft delete approach)
            val expense = expenseDao.getExpenseById(expenseId)
            if (expense != null) {
                expenseDao.updateExpense(expense.copy(synced = false, markedForDeletion = true)) // Mark as unsynced and flagged for deletion

                // Attempt deletion in Firebase if connected to the internet
                if (utility.isConnectedToInternet()) {
                    databaseReference.child(expenseId).removeValue().await()
                    expenseDao.deleteExpenseById(expenseId) // Delete locally if successful
                }
            }
            true // Local delete/update successful
        } catch (e: Exception) {
            Log.e("ExpenseRepository", "Error deleting expense: ${e.message}")
            false
        }
    }






}