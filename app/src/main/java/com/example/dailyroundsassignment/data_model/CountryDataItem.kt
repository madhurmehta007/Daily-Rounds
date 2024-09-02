package com.example.dailyroundsassignment.data_model

import com.google.gson.annotations.SerializedName

data class CountryDataItem(
    @SerializedName("country")
    val country: String,
    @SerializedName("region")
    val region: String
)