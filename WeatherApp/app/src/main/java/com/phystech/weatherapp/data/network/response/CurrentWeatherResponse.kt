package com.phystech.weatherapp.data.network.response

import com.google.gson.annotations.SerializedName
import com.phystech.weatherapp.data.db.entity.CurrentWeatherEntry
import com.phystech.weatherapp.data.db.entity.Location
import com.phystech.weatherapp.data.db.entity.Request


data class CurrentWeatherResponse(
    @SerializedName("current")
    val currentWeatherEntry: CurrentWeatherEntry,
    val location: Location,
    val request: Request
)