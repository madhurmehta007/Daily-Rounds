package com.example.dailyroundsassignment.ui.fragment.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyroundsassignment.data_model.CountryDataItem
import com.example.dailyroundsassignment.data_model.NetworkIPData
import com.example.dailyroundsassignment.networking.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignupViewModel@Inject constructor(private val repository: Repository) :ViewModel() {

    val networkResponse = MutableLiveData<Response<NetworkIPData>>()
    val networkDataResponse: LiveData<Response<NetworkIPData>>
        get() = networkResponse

    private val _countryListResponse = MutableLiveData<Response<List<CountryDataItem>>>()
    val countryListResponse: LiveData<Response<List<CountryDataItem>>>
        get() = _countryListResponse

    fun getCountryList(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getCountryList()
                _countryListResponse.postValue(response)
            } catch (e:IOException){
                Log.e("error", "${e.message}")
            }
        }
    }


}