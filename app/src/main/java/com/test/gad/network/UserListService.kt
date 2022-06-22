package com.test.gad.network

import com.test.gad.network.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface UserListService {
/*    @GET("forecast?lat=27.2046&lon=77.4977&appid=913b0654eb73e42e6264f316fb4a5797&cnt=30")
    suspend fun getWeatherList(): Response<WeatherResponse>*/

    @GET("forecast?appid=913b0654eb73e42e6264f316fb4a5797&cnt=30")
    suspend fun getWeatherList(@Query("lat") lat: String?,  @Query("lon") lon: String?): Response<WeatherResponse>

/*
    @GET("data/2.5/weather?")
    fun getCurrentWeatherData(
        @Query("lat") lat: String?,
        @Query("lon") lon: String?,
        @Query("APPID") app_id: String?
    ): Call<WeatherResponse?>?*/

}