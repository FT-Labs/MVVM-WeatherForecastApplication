package com.phystech.weatherapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.phystech.weatherapp.data.db.entity.CurrentWeatherEntry
import com.phystech.weatherapp.data.db.entity.Location
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized

@Database(
    entities = [CurrentWeatherEntry::class , Location::class],
    version = 2
)
abstract class ForecastWeatherDatabase : RoomDatabase(){

    abstract fun currentWeatherDao() : CurrentWeatherDAO
    abstract fun currentLocationDao() : WeatherLocationDAO

    @InternalCoroutinesApi
    companion object {
        @Volatile private var instance : ForecastWeatherDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK)
        {
            instance ?: buildDatabase(context).also { instance =  it}
        }

        private fun buildDatabase(context : Context) =
            Room.databaseBuilder(context.applicationContext, ForecastWeatherDatabase::class.java, "forecast.db")
                .build()

    }
}
