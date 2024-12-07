package com.example.flobiz_task



import com.example.flobiz_task.model.repository.ExpenseRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface SyncWorkerEntryPoint {
    fun expenseRepository(): ExpenseRepository
}


