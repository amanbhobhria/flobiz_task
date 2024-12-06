package com.example.flobiz_task.viewmodel

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flobiz_task.MyApp
import com.example.flobiz_task.model.data.Expense
import com.example.flobiz_task.model.repository.ExpenseRepository
import com.example.flobiz_task.utility.Event
import com.example.flobiz_task.view.AddNewTransctionActivity
import com.example.flobiz_task.view.ExpenseDetailActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository,
) : ViewModel() {

    private val _expenseList = MutableLiveData<List<Expense>>()
    val expenseList: LiveData<List<Expense>> = _expenseList

    private val _navigationEvent = MutableLiveData<Event<Intent>>()
    val navigationEvent: LiveData<Event<Intent>> get() = _navigationEvent

    init {
        fetchExpenses()
    }


    fun fetchExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getExpenses { expenses ->
                _expenseList.postValue(expenses)
            }
        }
    }


    fun onAddNewExpenseClicked() {
        val intent = Intent(MyApp.context, AddNewTransctionActivity::class.java)
        _navigationEvent.value = Event(intent)
    }


    fun onExpenseSelected(expense: Expense) {
        val intent = Intent(MyApp.context, ExpenseDetailActivity::class.java).apply {
            putExtra("expenseId", expense.id)
            putExtra("expenseType", expense.expenseType)
            putExtra("date", expense.date)
            putExtra("description", expense.description)
            putExtra("amount", expense.amount)
        }
        _navigationEvent.value = Event(intent)
    }


}