package com.phystech.weatherapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.phystech.weatherapp.data.db.entity.CURRENT_WEATHER_ID
import com.phystech.weatherapp.data.db.entity.CurrentWeatherEntry

@Dao
interface CurrentWeatherDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry: CurrentWeatherEntry)

    @Query("SELECT * FROM current_weather WHERE id=$CURRENT_WEATHER_ID")
    fun getWeatherMetric() : LiveData<CurrentWeatherEntry>

}