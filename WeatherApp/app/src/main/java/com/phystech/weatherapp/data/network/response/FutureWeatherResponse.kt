package com.phystech.weatherapp.data.network.response


import com.google.gson.annotations.SerializedName

data class FutureWeatherResponse(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Lists>,
    val message: Int
)