package com.example.flobiz_task.view



import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.flobiz_task.R
import com.example.flobiz_task.databinding.ActivityExpenseDetailBinding
import com.example.flobiz_task.model.data.Expense
import com.example.flobiz_task.viewmodel.ExpenseDetailViewModel

class ExpenseDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpenseDetailBinding
    private val viewModel: ExpenseDetailViewModel by viewModels()

    private var expenseId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.colorSearch)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // Retrieve passed expense details from intent
        expenseId = intent.getStringExtra("expenseId")
        val date = intent.getStringExtra("date") ?: ""
        val expenseType = intent.getStringExtra("expenseType") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        val amount = intent.getStringExtra("amount") ?: ""

        // Set initial data
        binding.expenseTypeTxt.text = expenseType
        binding.dateEdtTxt.setText(date)
        binding.descEdtTxt.setText(description)
        binding.amountEdtTxt.setText(amount)

        // Observe ViewModel state
        observeViewModel()

        // Set up button listeners
        binding.editBtn.setOnClickListener { viewModel.setEditable(true) }
        binding.saveBtn.setOnClickListener { saveExpense() }
        binding.deleteBtn.setOnClickListener { confirmDeletion() }
        binding.backIcon.setOnClickListener { finish() }
    }

    private fun observeViewModel() {
        viewModel.isEditable.observe(this, Observer { editable ->
            binding.dateEdtTxt.isEnabled = editable
            binding.dateEdtTxt.isFocusableInTouchMode = editable

            binding.descEdtTxt.isEnabled = editable
            binding.descEdtTxt.isFocusableInTouchMode = editable

            binding.amountEdtTxt.isEnabled = editable
            binding.amountEdtTxt.isFocusableInTouchMode = editable

            binding.editBtn.visibility = if (editable) View.GONE else View.VISIBLE
            binding.saveBtn.visibility = if (editable) View.VISIBLE else View.GONE
        })

        viewModel.updateStatus.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })

        viewModel.deleteStatus.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            if (message == "Expense deleted successfully!") finish()
        })
    }

    private fun saveExpense() {
        if (expenseId == null) {
            Toast.makeText(this, "Expense not found!", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedExpense = Expense(
            id = expenseId!!,
            expenseType = binding.expenseTypeTxt.text.toString(),
            date = binding.dateEdtTxt.text.toString(),
            description = binding.descEdtTxt.text.toString(),
            amount = binding.amountEdtTxt.text.toString()
        )

        viewModel.updateExpense(expenseId!!, updatedExpense)
    }

    private fun confirmDeletion() {
        AlertDialog.Builder(this)
            .setTitle("Delete Expense")
            .setMessage("Are you sure you want to delete this expense?")
            .setPositiveButton("Yes") { _, _ ->
                expenseId?.let { viewModel.deleteExpense(it) }
            }
            .setNegativeButton("No", null)
            .show()
    }
}



