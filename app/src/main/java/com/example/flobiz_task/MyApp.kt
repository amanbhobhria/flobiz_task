package com.example.flobiz_task


import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import java.lang.ref.WeakReference


@HiltAndroidApp
class MyApp : Application() {

    companion object {
        private var weakContext: WeakReference<Context>? = null

        val context: Context?
            get() = weakContext?.get()
    }

    override fun onCreate() {
        super.onCreate()
        weakContext = WeakReference(applicationContext)
    }


}