package com.phystech.weatherapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.phystech.weatherapp.data.db.entity.Location
import com.phystech.weatherapp.data.db.entity.WEATHER_LOCATION_ID

@Dao
interface WeatherLocationDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(location: Location)

    @Query("SELECT * FROM weather_location WHERE id = $WEATHER_LOCATION_ID")
    fun getLocation() : LiveData<Location>
}