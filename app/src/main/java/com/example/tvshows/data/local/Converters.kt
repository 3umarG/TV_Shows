package com.example.tvshows.data.local

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromEndDate(end_date: Any): String = end_date.toString()

    @TypeConverter
    fun toEndDate(end_date: String): Any = end_date as Any
}