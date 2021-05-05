package com.phystech.weatherapp.data.network.interfaces

import androidx.lifecycle.LiveData
import com.phystech.weatherapp.data.network.response.FutureWeatherResponse

interface FutureWeatherDataSource {
    val downloadedWeather : LiveData<FutureWeatherResponse>

    suspend fun fetchFutureWeather(
            location : String
    )
}