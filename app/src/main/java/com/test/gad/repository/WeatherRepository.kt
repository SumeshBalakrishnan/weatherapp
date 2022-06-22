package com.test.gad.repository

import com.test.gad.utils.NetworkResult
import com.test.gad.network.APIService
import com.test.gad.network.model.BaseApiResponse
import com.test.gad.network.model.WeatherResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@ActivityRetainedScoped
class WeatherRepository @Inject constructor(
    private val apiService: APIService
) : BaseApiResponse() {

    suspend fun getWeatherResponse(lat: String?, lng: String?): Flow<NetworkResult<WeatherResponse>> {
        return flow<NetworkResult<WeatherResponse>> {
            emit(safeApiCall { apiService.getWeatherList(lat, lng) })
        }.flowOn(Dispatchers.IO)
    }
}