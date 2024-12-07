package com.example.flobiz_task


import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.flobiz_task.model.repository.ExpenseRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class MyApp : Application() {

    companion object {
        lateinit var instance: MyApp
            private set

        val context: Context
            get() = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startNetworkCallback()
    }

    private fun startNetworkCallback() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                // Device is now connected to the internet
                syncOfflineExpenses()
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    // Sync the offline expenses once the network is available


    private fun syncOfflineExpenses() {
        val repository = getExpenseRepository()
        CoroutineScope(Dispatchers.IO).launch {
            repository.syncOfflineExpenses()
        }
    }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface AppEntryPoint {
        fun expenseRepository(): ExpenseRepository
    }

    private fun getExpenseRepository(): ExpenseRepository {
        val entryPoint = EntryPointAccessors.fromApplication(this, AppEntryPoint::class.java)
        return entryPoint.expenseRepository()
    }






}
