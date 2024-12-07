package com.example.flobiz_task.utility

import android.content.Context
import android.net.ConnectivityManager
import com.example.flobiz_task.MyApp

class Utility(private val context: Context) {

    fun isConnectedToInternet(): Boolean {
        val connectivityManager =
            MyApp.context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }









}