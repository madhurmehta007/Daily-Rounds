package com.example.dailyroundsassignment.utils

import androidx.room.TypeConverter
import com.example.dailyroundsassignment.data_model.BookData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {

    @TypeConverter
    fun fromBookList(value: ArrayList<BookData>): String {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<BookData>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toBookList(value: String): ArrayList<BookData> {
        val gson = Gson()
        val type = object : TypeToken<ArrayList<BookData>>() {}.type
        return gson.fromJson(value, type)
    }




}
