package com.example.dailyroundsassignment.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.dailyroundsassignment.data_model.BookData
import com.example.dailyroundsassignment.data_model.SavedBookData
import javax.inject.Inject


class BookDatabaseRepository @Inject constructor(
    private val bookDatabase: BookDatabase
) {
    private val bookDao = bookDatabase.bookDao()

    val allBookList: LiveData<MutableList<SavedBookData>> = bookDao.getAllBookData()

    @WorkerThread
    fun insertBooks(book: SavedBookData) {
        bookDao.insertFavoriteBookData(book)
    }

    @WorkerThread
    fun deleteBooks(id:String){
        bookDao.deleteBookData(id)
    }

}
