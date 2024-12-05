package com.example.flobiz_task.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.flobiz_task.R
import com.example.flobiz_task.databinding.ActivityMainBinding
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binidng:ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binidng = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binidng.root)


//        // Initialize Firebase Realtime Database
//        val database = FirebaseDatabase.getInstance()
//        val dbReference = database.reference
//
//        // Dummy data to upload
//        val dummyData = mapOf(
//            "name" to "John Doe",
//            "age" to 25,
//            "city" to "New York"
//        )
//
//        // Write data to Firebase
//        dbReference.child("users").child("user1").setValue(dummyData)
//            .addOnSuccessListener {
//                // Data uploaded successfully
//                Log.d("Firebase", "Data uploaded successfully")
//            }
//            .addOnFailureListener { exception ->
//                // Handle error
//                Log.e("Firebase", "Error uploading data", exception)
//            }




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