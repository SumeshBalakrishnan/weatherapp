package com.test.gad.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.gad.database.DatabaseUserListItem
import com.test.gad.database.UsersDatabase
import com.test.gad.utils.NetworkResult
import com.test.gad.network.model.WeatherResponse
import com.test.gad.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    public var allEvents: LiveData<List<DatabaseUserListItem>>? = null

    val testMutable = MutableLiveData<List<DatabaseUserListItem>>()
    val test : LiveData<List<DatabaseUserListItem>>
    get() = testMutable


    init {
       getDBdata()
    }

    fun getDBdata() {
        viewModelScope.launch(Dispatchers.IO) {
            val data =  weatherRepository.getAllWeather()
            testMutable.postValue(data)
        }
    }

    private val _response: MutableLiveData<NetworkResult<WeatherResponse>> = MutableLiveData()
    val response: LiveData<NetworkResult<WeatherResponse>> = _response

    fun fetchWeatherResponse(lat: String?, lng: String?) = viewModelScope.launch(Dispatchers.IO) {
        weatherRepository.getWeatherResponse(lat, lng).collect {
            _response.postValue(it)
            weatherRepository.clearEvent()
            val cityName = it.data?.city?.name
            for(item in it.data?.list!!){
                val humidity = item?.main?.humidity
                val tempMax = item?.main?.tempMax
                val tempMin = item?.main?.tempMin

                if(humidity != null){
                    Log.e("fetchWeatherResponse","==humidity===${humidity.toString()}" )
                    weatherRepository.insertWeather(DatabaseUserListItem(cityName,
                        humidity, tempMax, tempMin))
                }
            }

        }
    }
}