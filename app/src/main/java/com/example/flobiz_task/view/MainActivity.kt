package com.example.flobiz_task.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flobiz_task.R
import com.example.flobiz_task.databinding.ActivityMainBinding
import com.example.flobiz_task.viewmodel.GetExpenseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var expenseAdapter: GetExpenseAdapter
    private val viewModel: GetExpenseViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.colorSearch)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding.addNewBtn.setOnClickListener {
            viewModel.onAddNewExpenseClicked()
        }


        setupRecyclerView()
        observeData()


    }


    private fun setupRecyclerView() {
        expenseAdapter = GetExpenseAdapter { expense ->

            viewModel.onExpenseSelected(expense)

        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = expenseAdapter
        }


    }


    private fun observeData() {

        viewModel.navigationEvent.observe(this) { event ->
            event.getContentIfNotHandled()?.let { intent ->
                startActivity(intent)
            }
        }


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