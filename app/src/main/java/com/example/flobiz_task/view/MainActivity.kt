package com.example.flobiz_task.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flobiz_task.R
import com.example.flobiz_task.databinding.ActivityMainBinding
import com.example.flobiz_task.model.repository.ExpenseRepository
import com.example.flobiz_task.viewmodel.GetExpenseViewModel
import com.example.flobiz_task.viewmodel.GetExpenseViewModelFactory
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var expenseAdapter:GetExpenseAdapter
    private lateinit var viewModel: GetExpenseViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        viewModel = ViewModelProvider(this, GetExpenseViewModelFactory(ExpenseRepository()))[ GetExpenseViewModel::class.java]





        window.statusBarColor = ContextCompat.getColor(this, R.color.colorSearch)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        binding.addNewBtn.setOnClickListener{
            val intent = Intent(this, AddNewTransctionActivity::class.java)
            startActivity(intent)
        }

        binding.menuSettings.setOnClickListener {
            val intent = Intent(this, ExpenseDetailActivity::class.java)
            startActivity(intent)
        }

        setupRecyclerView()
        setupViewModel()
        observeData()




    }


    private fun setupRecyclerView() {
        expenseAdapter = GetExpenseAdapter{ expense ->
            val intent = Intent(this, ExpenseDetailActivity::class.java).apply {
                putExtra("expenseId", expense.id)
                putExtra("date", expense.date)
                putExtra("description", expense.description)
                putExtra("amount", expense.amount)
//                putExtra("id", expense)

            }
            startActivity(intent)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = expenseAdapter
        }
    }

    private fun setupViewModel() {
        val factory = GetExpenseViewModelFactory(ExpenseRepository())
        viewModel = ViewModelProvider(this, factory).get(GetExpenseViewModel::class.java)
        viewModel.fetchExpenses()
    }

    private fun observeData() {
        viewModel.expenseList.observe(this) { expenses ->
            if (expenses.isNotEmpty()) {
                expenseAdapter.submitList(expenses)



                Log.d("MainActivity", "Expenses: ${expenses.size}")
            } else {
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
            }
        }
    }


}