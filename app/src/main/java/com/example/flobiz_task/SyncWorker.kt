package com.example.flobiz_task

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.flobiz_task.model.repository.ExpenseRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@HiltWorker
class SyncWorker(
    @ApplicationContext private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {


    @Inject
    lateinit var repository: ExpenseRepository

    override suspend fun doWork(): Result {
        return try {
            repository.syncOfflineExpenses()
            Result.success()
        } catch (e: Exception) {
            Log.e("SyncWorker", "Error syncing offline data: ${e.message}")
            Result.failure()
        }
    }





}
