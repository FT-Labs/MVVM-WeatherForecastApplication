package com.phystech.weatherapp.data.network.response


import com.google.gson.annotations.SerializedName

data class Rain(
    @SerializedName("3h")
    val h: Double
)