package com.example.flobiz_task.view.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.flobiz_task.SyncWorker
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




        observeData()
        setupRecyclerView()
        setupSearchBar()
        syncOfflineData()

        binding.addNewBtn.setOnClickListener {
            viewModel.onAddNewExpenseClicked()
        }

        binding.menuSettings.setOnClickListener {

            viewModel.fetchExpenses() // This will fetch the data again

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



        try{
        viewModel.filteredExpenseList.observe(this) { expenses ->

           expenseAdapter.submitList(expenses)
        }}
         catch (e: Exception) {
            println("Error: ${e.message}")
             Toast.makeText(this,"${e.toString()+viewModel.errorMessage}", Toast.LENGTH_SHORT).show()
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


    private fun syncOfflineData() {
        val oneTimeSyncRequest = OneTimeWorkRequestBuilder<SyncWorker>().build()
        WorkManager.getInstance(applicationContext).enqueue(oneTimeSyncRequest)
    }


    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart called")
//        viewModel.fetchExpenses() // This will fetch the data again

    }


    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume called")
        viewModel.fetchExpenses() // This will fetch the data again
    }






}