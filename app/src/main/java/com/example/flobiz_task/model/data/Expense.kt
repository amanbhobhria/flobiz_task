package com.example.flobiz_task.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "expense")
data class Expense(
    @PrimaryKey
    var id: String ="",
    val expenseType: String = "",
    val date: String = "",
    val description: String = "",
    val amount: String = "",
    val synced: Boolean = false, // Indicates sync status
    val markedForDeletion: Boolean = false // Indicates if the expense is marked for deletion


)
