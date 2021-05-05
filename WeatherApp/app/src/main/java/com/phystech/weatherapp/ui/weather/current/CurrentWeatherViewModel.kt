package com.phystech.weatherapp.ui.weather.current

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.phystech.weatherapp.data.db.entity.CurrentWeatherEntry
import com.phystech.weatherapp.data.repository.ForecastRepository
import com.phystech.weatherapp.internal.lazyDeferred

class CurrentWeatherViewModel(
        private val forecastRepository: ForecastRepository

) : ViewModel() {
    val weather by lazyDeferred {
        forecastRepository.getCurrentWeather()
    }

    val location by lazyDeferred {
        forecastRepository.getLocation()
    }
}