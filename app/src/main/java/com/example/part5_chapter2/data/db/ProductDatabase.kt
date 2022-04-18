package com.example.part5_chapter2.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.part5_chapter2.data.db.dao.ProductDao
import com.example.part5_chapter2.data.entity.product.ProductEntity
import com.example.part5_chapter2.utillity.DateConverter

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(DateConverter::class)
abstract class ProductDatabase: RoomDatabase() {
    companion object{
        const val DB_NAME ="ProductDatabase.db"
    }
    abstract fun productDao(): ProductDao
}