package com.example.dailyroundsassignment.data_model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "bookData")
data class SavedBookData(
    @PrimaryKey
    val id: String,
    val favorite:Boolean = false
)
