package com.example.flobiz_task.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.flobiz_task.R
import com.example.flobiz_task.databinding.ActivityAddNewTransctionBinding
import com.example.flobiz_task.model.data.Expense
import com.example.flobiz_task.model.repository.AddNewExpenseRepository
import com.example.flobiz_task.viewmodel.AddNewExpenseViewModel
import com.example.flobiz_task.viewmodel.ExpenseViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNewTransctionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewTransctionBinding
    private lateinit var viewModel: AddNewExpenseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewTransctionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        window.statusBarColor = ContextCompat.getColor(this, R.color.colorSearch)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


        viewModel = ViewModelProvider(this, ExpenseViewModelFactory(AddNewExpenseRepository())).get(
            AddNewExpenseViewModel::class.java
        )


        // Setup listeners and observers
        setupListeners()
        setupRadioGroup()


    }

    private fun setupListeners() {
        binding.calenderBtn.setOnClickListener {
            showDatePicker()
        }

        binding.saveButton.setOnClickListener {

            saveExpense()
        }
    }


    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .build()

        datePicker.show(supportFragmentManager, "DATE_PICKER")


        datePicker.addOnPositiveButtonClickListener { selection ->
            val formatter = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
            val selectedDate = formatter.format(Date(selection))
            binding.editTextDate.setText(selectedDate)
        }
    }


    private fun saveExpense() {
        // Fetch data from binding
        val expenseType = if (binding.radioExpense.isChecked) "Expense" else "Income"
        val date = binding.editTextDate.text.toString().trim()
        val description = binding.descEdtTxt.text.toString().trim()
        val amount = binding.amountEdtTxt.text.toString().trim()

        // Validate input fields
        if (date.isEmpty() || amount.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create an expense object
        val expense = Expense(
            expenseType = expenseType,
            date = date,
            description = description,
            amount = amount
        )

        // Upload expense via ViewModel
        viewModel.uploadExpense(expense)

        // Observe the result of upload
        viewModel.uploadResult.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Expense saved successfully", Toast.LENGTH_SHORT).show()
                clearInputFields()
                finish()
            } else {
                Toast.makeText(this, "Failed to save expense. Try again.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun setupRadioGroup() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_expense -> {
                    // Update backgrounds
                    binding.radioExpense.setBackgroundResource(R.drawable.selected_radio_background)
                    binding.radioIncome.setBackgroundResource(R.drawable.radio_item_background)
                }

                R.id.radio_income -> {
                    // Update backgrounds
                    binding.radioIncome.setBackgroundResource(R.drawable.selected_radio_background)
                    binding.radioExpense.setBackgroundResource(R.drawable.radio_item_background)
                }
            }
        }
    }


    private fun clearInputFields() {
        binding.editTextDate.text.clear()
        binding.descEdtTxt.text.clear()
        binding.amountEdtTxt.text.clear()
        binding.radioExpense.isChecked = true
    }


}