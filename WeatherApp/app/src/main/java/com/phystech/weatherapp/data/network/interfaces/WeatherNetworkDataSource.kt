package com.phystech.weatherapp.data.network.interfaces

import androidx.lifecycle.LiveData
import com.phystech.weatherapp.data.network.response.CurrentWeatherResponse

interface WeatherNetworkDataSource
{
    val downloadedCurrentWeather : LiveData<CurrentWeatherResponse>

    suspend fun fetchCurrentWeather(
            location : String,
            languageCode : String
    )

}