package com.example.flobiz_task.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flobiz_task.model.data.Expense
import com.example.flobiz_task.model.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
        viewModelScope.launch {
            try {
                // Update expense in the repository
                repository.updateExpense(expense.copy(id = expenseId))

                // Notify the UI that the update was successful
                _updateStatus.postValue("Expense updated locally! Changes will sync when online.")
            } catch (e: Exception) {
                // Handle any errors during the update
                _updateStatus.postValue("Error updating expense: ${e.message}")
            }
        }
    }









    fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            try {
                // Attempt to delete the expense via the repository
                repository.deleteExpense(expenseId)

                // Notify the UI of the successful deletion (local deletion at minimum)
                _deleteStatus.postValue("Expense deleted locally! Changes will sync when online.")
            } catch (e: Exception) {
                // Handle any errors during the deletion
                _deleteStatus.postValue("Error deleting expense: ${e.message}")
            }
        }
    }





}
