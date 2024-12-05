package com.example.flobiz_task.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flobiz_task.model.repository.ExpenseRepository

class ExpenseViewModelFactory(private val repository: ExpenseRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNewExpenseViewModel::class.java)) {
            return AddNewExpenseViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}