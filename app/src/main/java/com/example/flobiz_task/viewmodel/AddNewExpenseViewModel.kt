package com.example.flobiz_task.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flobiz_task.model.data.Expense
import com.example.flobiz_task.model.repository.ExpenseRepository
import kotlinx.coroutines.launch

class AddNewExpenseViewModel(private val repository: ExpenseRepository): ViewModel() {

    private var expenseCount = 0
    private var incomeCount = 0


    private val _uploadResult = MutableLiveData<Boolean>()
    val uploadResult: LiveData<Boolean> get() = _uploadResult

    fun uploadExpense(expenseType: String, date: String, description: String, amount: String) {
        val id = if (expenseType.equals("expense", true)) {
            expenseCount += 1
            "Expense$expenseCount"
        } else {
            incomeCount += 1
            "Income$incomeCount"
        }
        val expense = Expense(id, expenseType, date, description, amount)
        viewModelScope.launch {
            val result = repository.uploadExpense(expense)
            _uploadResult.postValue(result)
        }
    }

    fun initializeCounters() {
        viewModelScope.launch {
            repository.getExpenses { expenses ->
                expenseCount = expenses.count { it.expenseType.equals("expense", true) }
                incomeCount = expenses.count { it.expenseType.equals("income", true) }
            }
        }

    }



}