package com.example.dailyroundsassignment.networking

import com.example.dailyroundsassignment.data_model.BookData
import com.example.dailyroundsassignment.data_model.CountryDataItem
import com.example.dailyroundsassignment.data_model.NetworkIPData
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("json")
    suspend fun getNetworkData(): Response<NetworkIPData>

    @GET("b/IU1K")
    suspend fun getCountryList(): Response<List<CountryDataItem>>

    @GET("b/CNGI")
    suspend fun getBookList(): Response<List<BookData>>
}