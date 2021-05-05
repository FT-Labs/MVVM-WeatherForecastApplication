package com.phystech.weatherapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.phystech.weatherapp.data.network.interfaces.WeatherNetworkDataSource
import com.phystech.weatherapp.data.network.response.CurrentWeatherResponse
import com.phystech.weatherapp.internal.NoConnectivityException

private val TAG = "Connectivity"

class WeatherNetworkDataSourceImpl(
        private val weatherApiService: WeatherApiService
) : WeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()

    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(location: String, languageCode: String) {
        try {
            val fetchedCurrentWeather = weatherApiService
                    .getCurrentWeather(location, languageCode)
                    .await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        }
        catch (e : NoConnectivityException)
        {
            Log.e(TAG, "No internet connection", e)
        }
    }
}