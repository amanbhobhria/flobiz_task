package com.example.flobiz_task.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.flobiz_task.model.data.Expense
import com.example.flobiz_task.model.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExpenseDetailViewModel @Inject constructor(private val repository: ExpenseRepository) : ViewModel() {


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
        repository.updateExpense(
            expenseId,
            expense,
            onSuccess = {
                _updateStatus.value = "Expense updated successfully!"
                setEditable(false)
            },
            onFailure = { error ->
                _updateStatus.value = error
            }
        )
    }




    fun deleteExpense(expenseId: String) {
        repository.deleteExpense(
            expenseId,
            onSuccess = {
                _deleteStatus.value = "Expense deleted successfully!"
            },
            onFailure = { error ->
                _deleteStatus.value = error
            }
        )
    }
}
