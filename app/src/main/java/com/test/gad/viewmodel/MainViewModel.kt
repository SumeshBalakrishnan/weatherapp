package com.test.gad.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.gad.utils.NetworkResult
import com.test.gad.network.model.WeatherResponse
import com.test.gad.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _response: MutableLiveData<NetworkResult<WeatherResponse>> = MutableLiveData()
    val response: LiveData<NetworkResult<WeatherResponse>> = _response

    fun fetchWeatherResponse(lat: String?, lng: String?) = viewModelScope.launch {
        weatherRepository.getWeatherResponse(lat, lng).collect {
            _response.value = it
        }
    }
}