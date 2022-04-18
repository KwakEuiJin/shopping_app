package com.example.part5_chapter2.data.db.dao

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.part5_chapter2.data.db.ProductDatabase

internal fun provideDB(context:Context):ProductDatabase =
    Room.databaseBuilder(context,ProductDatabase::class.java,ProductDatabase.DB_NAME).build()

internal fun provideTodoDao(database: ProductDatabase)=database.productDao()