package com.example.flobiz_task.view.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flobiz_task.databinding.ActivityMainBinding
import com.example.flobiz_task.view.adapter.GetExpenseAdapter
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


        setupRecyclerView()
        observeData()
        setupSearchBar()

        binding.addNewBtn.setOnClickListener {
            viewModel.onAddNewExpenseClicked()
        }


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

        viewModel.filteredExpenseList.observe(this) { expenses ->
            expenseAdapter.submitList(expenses)
        }


        viewModel.navigationEvent.observe(this) { event ->
            event.getContentIfNotHandled()?.let { intent ->
                startActivity(intent)
            }
        }

    }

    private fun setupSearchBar() {
        binding.searchBar.addTextChangedListener { text1 ->
            val query = text1.toString()
            viewModel.filterExpenses(query)

        }

    }


}