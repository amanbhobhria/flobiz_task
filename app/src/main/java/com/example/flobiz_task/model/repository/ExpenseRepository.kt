package com.example.flobiz_task.model.repository

import com.example.flobiz_task.model.data.Expense
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class ExpenseRepository  @Inject constructor(){
private val databaseReference = FirebaseDatabase.getInstance().getReference("expenses")



    suspend fun uploadExpense(expense: Expense): Boolean {
        return try {


            val key = databaseReference.push().key ?: throw Exception("Unable to generate key")
            expense.id = key
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


    fun updateExpense(expenseId: String, expense: Expense, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        databaseReference.child(expenseId).setValue(expense)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception.message ?: "Failed to update expense") }
    }

    fun deleteExpense(expenseId: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        databaseReference.child(expenseId).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception.message ?: "Failed to delete expense") }
    }





}