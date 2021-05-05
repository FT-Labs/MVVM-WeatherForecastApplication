package com.phystech.weatherapp.ui.weather.future.list

import androidx.lifecycle.ViewModel
import com.phystech.weatherapp.data.repository.ForecastRepository
import com.phystech.weatherapp.internal.lazyDeferred


class FutureListWeatherViewModel(
        private val forecastRepository: ForecastRepository
) : ViewModel() {
    val futureWeather by lazyDeferred {
        forecastRepository.getFutureWeather()
    }

    val location by lazyDeferred {
        forecastRepository.getLocation()
    }
}