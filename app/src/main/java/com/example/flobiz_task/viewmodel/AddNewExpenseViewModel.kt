package com.example.flobiz_task.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flobiz_task.model.data.Expense
import com.example.flobiz_task.model.repository.AddNewExpenseRepository
import kotlinx.coroutines.launch

class AddNewExpenseViewModel(private val repository: AddNewExpenseRepository): ViewModel() {

    private val _uploadResult = MutableLiveData<Boolean>()
    val uploadResult: LiveData<Boolean> get() = _uploadResult

    fun uploadExpense(expense: Expense) {
        viewModelScope.launch {
            val result = repository.uploadExpense(expense)
            _uploadResult.postValue(result)
        }
    }


}