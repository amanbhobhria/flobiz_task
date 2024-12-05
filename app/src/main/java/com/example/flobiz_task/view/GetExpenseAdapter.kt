package com.example.flobiz_task.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.flobiz_task.databinding.TransctionItemBinding
import com.example.flobiz_task.model.data.Expense

class GetExpenseAdapter(private val onItemClick: (Expense) -> Unit)
    : ListAdapter<Expense, GetExpenseAdapter.ExpenseViewHolder>(ExpenseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = TransctionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = getItem(position)
        holder.bind(expense)
        holder.itemView.setOnClickListener { onItemClick(expense) }
    }

    class ExpenseViewHolder(private val binding: TransctionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(expense: Expense) {
            binding.expenseId.text = expense.id
            binding.expenseDate.text = expense.date
            binding.expenseDescription.text = expense.description
            binding.expenseAmount.text = expense.amount
        }
    }

    class ExpenseDiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            // Compare unique identifiers, if any; otherwise compare entire object
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            // Compare all fields of the objects
            return oldItem == newItem
        }
    }
}