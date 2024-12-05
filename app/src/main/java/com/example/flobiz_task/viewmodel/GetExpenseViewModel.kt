package com.example.flobiz_task.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flobiz_task.model.data.Expense
import com.example.flobiz_task.model.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GetExpenseViewModel (private val repository: ExpenseRepository) : ViewModel() {

    private val _expenseList = MutableLiveData<List<Expense>>()
    val expenseList: LiveData<List<Expense>> = _expenseList

    fun fetchExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getExpenses { expenses ->
                _expenseList.postValue(expenses)
            }
        }
    }
}