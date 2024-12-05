package com.example.flobiz_task.model.repository

import com.example.flobiz_task.model.data.Expense
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await



class AddNewExpenseRepository {



private val databaseReference = FirebaseDatabase.getInstance().getReference("expenses")

// Function to upload data to Firebase
suspend fun uploadExpense(expense: Expense): Boolean {
    return try {
        val key = databaseReference.push().key ?: throw Exception("Unable to generate key")
        databaseReference.child(key).setValue(expense).await()
        true
    } catch (e: Exception) {
        false
    }
}

}