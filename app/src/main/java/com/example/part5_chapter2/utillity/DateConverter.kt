package com.example.part5_chapter2.utillity

import androidx.room.TypeConverter
import java.util.*

object DateConverter {

    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return if (dateLong == null) null else Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

}