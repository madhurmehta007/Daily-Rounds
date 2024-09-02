package com.example.dailyroundsassignment.networking


import com.example.dailyroundsassignment.data_model.BookData
import com.example.dailyroundsassignment.data_model.CountryDataItem
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(
    private val apiInterface: ApiInterface,
) {

    suspend fun getCountryList(): Response<List<CountryDataItem>>{
        return apiInterface.getCountryList()
    }

    suspend fun getBookList(): Response<List<BookData>>{
        return apiInterface.getBookList()
    }

}