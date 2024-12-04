package com.example.flobiz_task

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.flobiz_task.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binidng:ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binidng = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binidng.root)

        binidng.addNewBtn.setOnClickListener{
            val intent = Intent(this, AddNewTransctionActivity::class.java)
            startActivity(intent)
        }

        binidng.menuSettings.setOnClickListener {
            val intent = Intent(this, ExpenseDetailActivity::class.java)
            startActivity(intent)
        }



        window.statusBarColor = ContextCompat.getColor(this, R.color.colorSearch)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR


    }
}