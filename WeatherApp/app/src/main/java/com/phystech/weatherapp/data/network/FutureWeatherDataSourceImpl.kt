package com.phystech.weatherapp.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.phystech.weatherapp.data.network.interfaces.FutureWeatherDataSource
import com.phystech.weatherapp.data.network.response.FutureWeatherResponse
import com.phystech.weatherapp.internal.NoConnectivityException

private const val TAG = "FutureWeatherDataSource"

class FutureWeatherDataSourceImpl(
        private val futureWeatherApiService: FutureWeatherApiService
) : FutureWeatherDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<FutureWeatherResponse>()

    override val downloadedWeather: LiveData<FutureWeatherResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchFutureWeather(location: String) {
        try {
            val fetchedFutureWeather = futureWeatherApiService
                    .getFutureWeather(location)
                    .await()
            _downloadedCurrentWeather.postValue(fetchedFutureWeather)
        }
        catch (e : NoConnectivityException)
        {
            Log.e(TAG, "No internet connection", e)
        }
    }
}