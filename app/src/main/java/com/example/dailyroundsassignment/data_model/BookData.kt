package com.example.dailyroundsassignment.data_model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Calendar
import java.util.Date


data class BookData(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: String?,
    @SerializedName("popularity")
    val popularity: Int?,
    @SerializedName("publishedChapterDate")
    val publishedChapterDate: Long,
    @SerializedName("score")
    val score: Double?,
    @SerializedName("title")
    val title: String?,

    var favorite:Boolean = false
):Serializable{
    val year: Int
        get() = convertEpochToYear(publishedChapterDate)

    companion object {
        private fun convertEpochToYear(epochTime: Long): Int {
            val date = Date(epochTime * 1000)
            val calendar = Calendar.getInstance()
            calendar.time = date
            return calendar.get(Calendar.YEAR)
        }
    }
}