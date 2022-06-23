package com.test.gad.repository

import androidx.lifecycle.LiveData
import com.test.gad.database.DatabaseUserListItem
import com.test.gad.database.UsersDatabase
import com.test.gad.network.APIService
import com.test.gad.network.model.BaseApiResponse
import com.test.gad.network.model.WeatherResponse
import com.test.gad.utils.NetworkResult
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityRetainedScoped
class WeatherRepository @Inject constructor(
    private val apiService: APIService,
    private val usersDatabase: UsersDatabase
) : BaseApiResponse() {

    suspend fun getWeatherResponse(
        lat: String?,
        lng: String?
    ): Flow<NetworkResult<WeatherResponse>> {
        return flow<NetworkResult<WeatherResponse>> {
            emit(safeApiCall {
                apiService.getWeatherList(lat, lng)
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun insertWeather(cityName: DatabaseUserListItem) {
        usersDatabase.usersDao.insert(cityName)
    }

    suspend fun insertWeather1(cityName: DatabaseUserListItem) {
        usersDatabase.usersDao.insert(cityName)
    }

    suspend fun getAllWeather() : List<DatabaseUserListItem>{
        return usersDatabase.usersDao.getAllWeather()
    }

    // delete all events
    suspend fun clearEvent() = usersDatabase.usersDao.clearDatabaseUserListItem()
}