package com.example.flobiz_task.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flobiz_task.model.data.Expense
import com.google.firebase.database.FirebaseDatabase

class ExpenseDetailViewModel : ViewModel() {
    private val databaseReference = FirebaseDatabase.getInstance().getReference("expenses")

    private val _isEditable = MutableLiveData(false)
    val isEditable: LiveData<Boolean> get() = _isEditable

    private val _updateStatus = MutableLiveData<String>()
    val updateStatus: LiveData<String> get() = _updateStatus

    private val _deleteStatus = MutableLiveData<String>()
    val deleteStatus: LiveData<String> get() = _deleteStatus

    fun setEditable(editable: Boolean) {
        _isEditable.value = editable
    }

    fun updateExpense(expenseId: String, expense: Expense) {
        databaseReference.child(expenseId).setValue(expense)
            .addOnSuccessListener {
                _updateStatus.value = "Expense updated successfully!"
                setEditable(false)
            }
            .addOnFailureListener {
                _updateStatus.value = "Failed to update expense!"
            }
    }

    fun deleteExpense(expenseId: String) {
        databaseReference.child(expenseId).removeValue()
            .addOnSuccessListener {
                _deleteStatus.value = "Expense deleted successfully!"
            }
            .addOnFailureListener {
                _deleteStatus.value = "Failed to delete expense!"
            }
    }
}
