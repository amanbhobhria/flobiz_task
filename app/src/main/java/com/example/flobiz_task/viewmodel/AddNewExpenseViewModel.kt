package com.example.flobiz_task.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flobiz_task.model.data.Expense
import com.example.flobiz_task.model.repository.ExpenseRepository
import com.google.firebase.FirebaseException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class AddNewExpenseViewModel  @Inject constructor(private val repository: ExpenseRepository): ViewModel() {
     private val _uploadResult = MutableLiveData<Boolean>()
    val uploadResult: LiveData<Boolean> get() = _uploadResult


    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage



    fun uploadExpense(expenseType: String, date: String, description: String, amount: String) {
        val expense = Expense(
            id = UUID.randomUUID().toString(),
            expenseType = expenseType,
            date = date,
            description = description,
            amount = amount
        )

        viewModelScope.launch {
            try {
                val result = repository.uploadExpense(expense)
                _uploadResult.postValue(result)
            } catch (e: Exception) {
                when (e) {
                    is IOException -> _errorMessage.postValue("Network error. Please try again.")
                    is FirebaseException -> _errorMessage.postValue("Failed to connect to the database.")
                    else -> _errorMessage.postValue("An unexpected error occurred: ${e.message}")
                }
                _uploadResult.postValue(false)
            }

        }
    }





}