package com.phystech.weatherapp.ui.weather.future.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.phystech.weatherapp.data.repository.ForecastRepository

class FutureListWeatherViewModelFactory (
        private val forecastRepository: ForecastRepository
        ) : ViewModelProvider.NewInstanceFactory(){

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return FutureListWeatherViewModel(forecastRepository) as T
        }
}