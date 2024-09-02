package com.example.dailyroundsassignment.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dailyroundsassignment.data_model.BookData
import com.example.dailyroundsassignment.data_model.SavedBookData
import com.example.dailyroundsassignment.utils.Converter

@Database(entities = [SavedBookData::class], version = 2)
@TypeConverters(Converter::class)
abstract class BookDatabase:RoomDatabase() {
    abstract fun bookDao() : BookDao
}