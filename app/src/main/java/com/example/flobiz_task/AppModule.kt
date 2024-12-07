package com.example.flobiz_task

import android.content.Context
import androidx.work.WorkManager
import com.example.flobiz_task.model.data.ExpenseDao
import com.example.flobiz_task.model.data.ExpenseDatabase
import com.example.flobiz_task.model.repository.ExpenseRepository
import com.example.flobiz_task.utility.Utility
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    fun provideDatabaseReference(firebaseDatabase: FirebaseDatabase): DatabaseReference {
        return firebaseDatabase.getReference("expenses")
    }


    @Singleton
    @Provides
    fun provideUtility(@ApplicationContext context: Context): Utility {
        return Utility(context)
    }



//    @Singleton
//    @Provides
//    fun provideDatabase(@ApplicationContext context: Context): ExpenseDatabase {
//        return Room.databaseBuilder(context, ExpenseDatabase::class.java, "expense_database")
//            .fallbackToDestructiveMigration()
//            .build()
//    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ExpenseDatabase {
        return ExpenseDatabase.getDatabase(context) // Call getDatabase here
    }


    @Provides
    fun provideExpenseDao(database: ExpenseDatabase): ExpenseDao {
        return database.expenseDao()
    }

    @Provides
    fun provideExpenseRepository(
        databaseReference: DatabaseReference,
        expenseDao: ExpenseDao,
        utility: Utility,
    ): ExpenseRepository {
        return ExpenseRepository(databaseReference, expenseDao, utility)
    }





    @Singleton
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }


    @Singleton
    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }


    @Singleton
    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }





}
