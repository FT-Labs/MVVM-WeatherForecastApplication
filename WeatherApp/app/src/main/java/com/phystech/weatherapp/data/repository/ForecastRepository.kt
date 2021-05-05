package com.phystech.weatherapp.data.repository

import androidx.lifecycle.LiveData
import com.phystech.weatherapp.data.db.entity.CurrentWeatherEntry
import com.phystech.weatherapp.data.db.entity.Location
import com.phystech.weatherapp.data.network.response.FutureWeatherResponse

interface ForecastRepository {
    suspend fun getCurrentWeather() : LiveData<CurrentWeatherEntry>

    suspend fun getLocation() : LiveData<Location>

    suspend fun getFutureWeather() : LiveData<FutureWeatherResponse>

}