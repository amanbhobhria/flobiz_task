package com.example.flobiz_task.view.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.flobiz_task.R
import com.example.flobiz_task.databinding.ActivityAddNewTransctionBinding
import com.example.flobiz_task.viewmodel.AddNewExpenseViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class AddNewTransctionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewTransctionBinding
    private val viewModel: AddNewExpenseViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewTransctionBinding.inflate(layoutInflater)
        setContentView(binding.root)



        viewModel.initializeCounters()

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
        // Retrieve input values
        val expenseType = if (binding.radioExpense.isChecked) "Expense" else "Income"
        val date = binding.editTextDate.text.toString().trim()
        val description = binding.descEdtTxt.text.toString().trim()
        val amount = binding.amountEdtTxt.text.toString().trim()

        // Validate input fields
        if (date.isEmpty() || amount.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }


              // Upload expense via ViewModel
        viewModel.uploadExpense(expenseType, date, description, amount)

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