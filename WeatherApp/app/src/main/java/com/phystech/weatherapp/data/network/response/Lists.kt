package com.phystech.weatherapp.data.network.response


import com.google.gson.annotations.SerializedName

data class Lists(
    val clouds: Clouds,
    val dt: Int,
    @SerializedName("dt_txt")
    val dtTxt: String,
    val main: Main,
    val pop: Double,
    val rain: Rain,
    val sys: Sys,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)