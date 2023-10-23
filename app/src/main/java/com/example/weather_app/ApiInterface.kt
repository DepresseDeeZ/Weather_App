package com.example.weather_app
import retrofit2.http.GET
import retrofit2.http.Query

import android.telecom.Call

interface ApiInterface {
    @GET("weather")
    fun  getWeatherData(
        @Query("q") city:String,
        @Query("appid") appid:String,
        @Query("units") units:String,
    ) : retrofit2.Call<WeatherApp>
}