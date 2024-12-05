package com.example.flobiz_task.model.repository

import com.example.flobiz_task.model.data.Expense
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await



class ExpenseRepository {



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


    fun getExpenses(callback: (List<Expense>) -> Unit) {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val expenseList = mutableListOf<Expense>()
                for (child in snapshot.children) {
                    val expense = child.getValue(Expense::class.java)
                    if (expense != null) expenseList.add(expense)
                }
                callback(expenseList)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(emptyList())
            }
        })
    }



}