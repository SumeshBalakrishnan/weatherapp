package com.test.gad.network

import com.test.gad.network.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface APIService {
    @GET("forecast?appid=913b0654eb73e42e6264f316fb4a5797&cnt=30")
    suspend fun getWeatherList(@Query("lat") lat: String?,  @Query("lon") lon: String?): Response<WeatherResponse>
}