package com.test.gad.network

import com.test.gad.network.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Query

interface UserListService {
  /*  @GET("forecast?lat={lat}&lon={lng}&appid=913b0654eb73e42e6264f316fb4a5797&cnt=30")
    suspend fun getWeatherList(@Path("lat") lat: Double?,
                               @Path("lng") lng: Double?): Response<WeatherResponse>*/

    @GET("forecast?lat=27.2046&lon=77.4977&appid=913b0654eb73e42e6264f316fb4a5797&cnt=30")
    suspend fun getWeatherList(): Response<WeatherResponse>

}