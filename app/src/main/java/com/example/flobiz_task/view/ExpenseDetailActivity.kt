package com.example.flobiz_task.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.flobiz_task.R
import com.example.flobiz_task.databinding.ActivityExpenseDetailBinding
import com.example.flobiz_task.model.data.Expense
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ExpenseDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpenseDetailBinding
    private var expenseId: String? = null // Unique ID of the expense (e.g., expense1, income1)
    private var isEditable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.colorSearch)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // Retrieve passed expense details from intent
        expenseId = intent.getStringExtra("expenseId")
        Log.d("expenseDetail", "Expense ID: $expenseId")
        val date = intent.getStringExtra("date") ?: ""
        val expenseType = intent.getStringExtra("expenseType") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        val amount = intent.getStringExtra("amount") ?: ""




        // Set initial data
        binding.expenseTypeTxt.text = expenseType
        binding.dateEdtTxt.setText(date)
        binding.descEdtTxt.setText(description)
        binding.amountEdtTxt.setText(amount)

        // Make fields non-editable initially
        toggleEditable(false)

        // Set up button listeners
        binding.editBtn.setOnClickListener { toggleEditable(true) }
        binding.saveBtn.setOnClickListener { updateExpense() }
        binding.deleteBtn.setOnClickListener { confirmDeletion() }
        binding.backIcon.setOnClickListener{
            finish()
        }
    }

    private fun toggleEditable(editable: Boolean) {
        isEditable = editable

        Log.d("expenseDetail", "editable: $editable")
        binding.dateEdtTxt.isEnabled = isEditable
        binding.dateEdtTxt.isFocusable = isEditable
        binding.dateEdtTxt.isFocusableInTouchMode = isEditable

        binding.descEdtTxt.isEnabled = isEditable
        binding.descEdtTxt.isFocusable = isEditable
        binding.descEdtTxt.isFocusableInTouchMode = isEditable

        binding.amountEdtTxt.isEnabled = isEditable
        binding.amountEdtTxt.isFocusable = isEditable
        binding.amountEdtTxt.isFocusableInTouchMode = isEditable





        if (editable) {
            Log.d("expenseDetail",isEditable.toString())


            binding.editBtn.visibility = View.GONE
            binding.saveBtn.visibility = View.VISIBLE
        } else {
            Log.d("expenseDetail",editable.toString())
            binding.dateEdtTxt.isEnabled = isEditable
            binding.descEdtTxt.isEnabled = isEditable
            binding.amountEdtTxt.isEnabled = isEditable
            binding.editBtn.visibility = View.VISIBLE
            binding.saveBtn.visibility = View.GONE
        }
    }

    private fun updateExpense() {
        if (expenseId == null) {
            Toast.makeText(this, "Expense not found!", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedExpense = Expense(
            id = expenseId.toString(),
            expenseType = binding.expenseTypeTxt.text.toString(),
            date = binding.dateEdtTxt.text.toString(),
            description = binding.descEdtTxt.text.toString(),
            amount = binding.amountEdtTxt.text.toString()
        )

        Log.d("expenseDetail", "in Update Expense ID: $expenseId")
        val databaseReference = FirebaseDatabase.getInstance().getReference("expenses")
        databaseReference.child(expenseId!!).setValue(updatedExpense)
            .addOnSuccessListener {
                Toast.makeText(this, "Expense updated successfully!", Toast.LENGTH_SHORT).show()
                toggleEditable(false)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update expense!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun confirmDeletion() {
        AlertDialog.Builder(this)
            .setTitle("Delete Expense")
            .setMessage("Are you sure you want to delete this expense?")
            .setPositiveButton("Yes") { _, _ -> deleteExpense() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteExpense() {
        if (expenseId == null) {
            Toast.makeText(this, "Expense not found!", Toast.LENGTH_SHORT).show()
            return
        }

        val databaseReference = FirebaseDatabase.getInstance().getReference("expenses")
        databaseReference.child(expenseId!!).removeValue()
            .addOnSuccessListener {

                Toast.makeText(this, "Expense deleted successfully!", Toast.LENGTH_SHORT).show()
                finish() // Close the activity after deletion
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to delete expense!", Toast.LENGTH_SHORT).show()
            }
    }






}
