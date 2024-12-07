package com.example.flobiz_task.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flobiz_task.model.data.Expense
import com.example.flobiz_task.model.repository.ExpenseRepository
import com.example.flobiz_task.utility.Event
import com.example.flobiz_task.view.activity.AddNewTransctionActivity
import com.example.flobiz_task.view.activity.ExpenseDetailActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class GetExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _expenseList = MutableLiveData<List<Expense>>()
    val expenseList: LiveData<List<Expense>> = _expenseList

    private val _navigationEvent = MutableLiveData<Event<Intent>>()
    val navigationEvent: LiveData<Event<Intent>> get() = _navigationEvent

    private val _filteredExpenseList = MutableLiveData<List<Expense>>()
    val filteredExpenseList: LiveData<List<Expense>> = _filteredExpenseList


    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _syncStatus = MutableLiveData<String>()
    val syncStatus: LiveData<String> get() = _syncStatus


    init {
        fetchExpenses()
    }


    fun fetchExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val expenseList = repository.getExpenses()
                _expenseList.postValue(expenseList)
                _filteredExpenseList.postValue(expenseList)
            } catch (e: Exception) {
                _errorMessage.postValue("Error: ${e.message}")
            }
        }
    }


    fun onAddNewExpenseClicked() {
        val intent = Intent(context, AddNewTransctionActivity::class.java)
        _navigationEvent.value = Event(intent)
    }


    fun onExpenseSelected(expense: Expense) {
        val intent = Intent(context, ExpenseDetailActivity::class.java).apply {
            putExtra("expenseId", expense.id)
            putExtra("expenseType", expense.expenseType)
            putExtra("date", expense.date)
            putExtra("description", expense.description)
            putExtra("amount", expense.amount)
        }
        _navigationEvent.value = Event(intent)
    }

    fun filterExpenses(query: String) {
        val currentExpenses = _expenseList.value ?: emptyList()
        val filtered = if (query.isBlank()) {
            currentExpenses
        } else {
            currentExpenses.filter { it.description.contains(query, ignoreCase = true) }
        }
        _filteredExpenseList.value = filtered
    }


}