package com.phystech.weatherapp.provider

import android.content.Context
import com.phystech.weatherapp.data.db.entity.Location

interface LocationProvider {
    suspend fun isLocationChanged(lastWeatherLocation : Location) : Boolean
    suspend fun getPreferredLocationString() : String
    fun getContext() : Context
}