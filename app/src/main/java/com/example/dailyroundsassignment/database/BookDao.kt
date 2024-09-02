package com.example.dailyroundsassignment.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.dailyroundsassignment.data_model.BookData
import com.example.dailyroundsassignment.data_model.SavedBookData

@Dao
interface BookDao {

    @Query("SELECT * FROM bookData")
    fun getAllBookData(): LiveData<MutableList<SavedBookData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteBookData(vararg favoriteBookDataList:SavedBookData)

    @Query("DELETE FROM bookData WHERE id = :id")
    fun deleteBookData(id:String)

}