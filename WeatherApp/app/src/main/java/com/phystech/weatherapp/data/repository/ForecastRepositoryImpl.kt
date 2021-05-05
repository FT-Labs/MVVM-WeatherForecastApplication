package com.phystech.weatherapp.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.phystech.weatherapp.data.db.entity.CurrentWeatherEntry
import com.phystech.weatherapp.data.db.CurrentWeatherDAO
import com.phystech.weatherapp.data.db.WeatherLocationDAO
import com.phystech.weatherapp.data.db.entity.Location
import com.phystech.weatherapp.data.network.interfaces.FutureWeatherDataSource
import com.phystech.weatherapp.data.network.interfaces.WeatherNetworkDataSource
import com.phystech.weatherapp.data.network.response.CurrentWeatherResponse
import com.phystech.weatherapp.data.network.response.FutureWeatherResponse
import com.phystech.weatherapp.provider.LocationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "ForecastRepository"

class ForecastRepositoryImpl(
        private val currentWeatherDAO : CurrentWeatherDAO,
        private val currentLocationDao: WeatherLocationDAO,
        private val locationProvider: LocationProvider,
        private val weatherNetworkDataSource : WeatherNetworkDataSource,
        private val futureWeatherDataSource: FutureWeatherDataSource
) : ForecastRepository {



    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }

    }


    private fun persistFetchedCurrentWeather(fetchedWeather : CurrentWeatherResponse)
    {
        GlobalScope.launch(Dispatchers.IO) {

            try{
                currentWeatherDAO.upsert(fetchedWeather.currentWeatherEntry)
                currentLocationDao.upsert(fetchedWeather.location)
            }
            catch(e : Exception)
            {
                Log.d(TAG, "Error in fetching data(Wrong city name?)")
            }
        }
    }

    override suspend fun getFutureWeather(): LiveData<FutureWeatherResponse> {
        return withContext(Dispatchers.IO) {
            return@withContext futureWeatherDataSource.downloadedWeather
        }
    }




    override suspend fun getLocation(): LiveData<Location> {
        return withContext(Dispatchers.IO) {
            return@withContext currentLocationDao.getLocation()
        }
    }



    override suspend fun getCurrentWeather(): LiveData<CurrentWeatherEntry> {
        //withContext returns a value, GlobalScope doesnt returns a value
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext currentWeatherDAO.getWeatherMetric()
        }
    }

    private suspend fun fetchCurrentWeather()
    {
        weatherNetworkDataSource.fetchCurrentWeather(
                locationProvider.getPreferredLocationString(),
                Locale.getDefault().language
        )

        futureWeatherDataSource.fetchFutureWeather(
                locationProvider.getPreferredLocationString()
        )

    }

    private suspend fun initWeatherData()
    {
        val loc = currentLocationDao.getLocation().value

        if (loc == null || locationProvider.isLocationChanged(loc))
        {
            fetchCurrentWeather()
            return
        }

        if (isFetchCurrentNeeded(loc.zonedDateTime))
        {
            fetchCurrentWeather()
        }

    }

    private fun isFetchCurrentNeeded(lastFetchTime : ZonedDateTime) : Boolean {
        val thirthyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirthyMinutesAgo)
    }
}