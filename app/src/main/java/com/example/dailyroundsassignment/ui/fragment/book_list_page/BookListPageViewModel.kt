package com.example.dailyroundsassignment.ui.fragment.book_list_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyroundsassignment.data_model.BookData
import com.example.dailyroundsassignment.data_model.SavedBookData
import com.example.dailyroundsassignment.database.BookDatabaseRepository
import com.example.dailyroundsassignment.networking.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookListPageViewModel @Inject constructor(private val repository: Repository, private val dbRepository: BookDatabaseRepository) : ViewModel() {

    private val _booksByYear = MutableLiveData<Map<Int, List<BookData>>>()
    val booksByYear: LiveData<Map<Int, List<BookData>>> get() = _booksByYear

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    val allSavedBooks: LiveData<MutableList<SavedBookData>> = dbRepository.allBookList

    fun loadBooks() {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try{
                val books = repository.getBookList().body() ?: emptyList()
                val groupedBooks = books.groupBy { it.year }
                _booksByYear.postValue(groupedBooks)
            } finally {
                _isLoading.postValue(false)
            }

        }
    }

   private fun insertBooks(bookData: SavedBookData) {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepository.insertBooks(bookData)
        }
    }

    private fun deleteBooks(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dbRepository.deleteBooks(id)
        }
    }

    fun getFavoriteBooksForYear(year: Int): List<BookData> {
        val booksForYear = booksByYear.value?.get(year) ?: emptyList()
        val favoriteIds = allSavedBooks.value?.map { it.id }?.toSet() ?: emptySet()
        return booksForYear.map { apiBook ->
            apiBook.copy(favorite = favoriteIds.contains(apiBook.id))
        }
    }

    fun handleFavoriteClick(book: BookData) {
        val existingBook = allSavedBooks.value?.find { it.id == book.id }
        if (existingBook != null) {
            deleteBooks(book.id)
        } else {
            insertBooks(SavedBookData(id = book.id, favorite = true))
        }
    }
}
