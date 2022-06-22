package com.test.gad.repository

import com.test.gad.utils.NetworkResult
import com.test.gad.network.UserListService
import com.test.gad.network.model.BaseApiResponse
import com.test.gad.network.model.WeatherResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@ActivityRetainedScoped
class UserListRepository @Inject constructor(
    private val userListService: UserListService
) : BaseApiResponse() {

    suspend fun getWeatherResponse(): Flow<NetworkResult<WeatherResponse>> {
        return flow<NetworkResult<WeatherResponse>> {
            emit(safeApiCall { userListService.getWeatherList() })
        }.flowOn(Dispatchers.IO)
    }
}