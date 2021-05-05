package com.phystech.weatherapp.ui.weather.future.list

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.phystech.weatherapp.R
import com.phystech.weatherapp.data.network.response.FutureWeatherResponse
import com.phystech.weatherapp.data.network.response.Lists
import com.phystech.weatherapp.internal.glide.GlideApp
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_future_weather.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class FutureWeatherItem(
        val weatherEntry : Lists
) : Item() {

    companion object {
        val itemLayout = R.layout.item_future_weather
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            textView_condition.text = weatherEntry.weather[0].description.toString()
            updateDate()
            updateImage()
            updateTemperature()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun ViewHolder.updateDate() {
        val tmp = weatherEntry.dtTxt.split(" ")
        val dtFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
        textView_date.text = tmp[0].format(dtFormatter)
        textView_dayTime.text = tmp[1].substring(0,5)
    }

    @SuppressLint("SetTextI18n")
    private fun ViewHolder.updateTemperature() {
        textView_temperature.text = "${weatherEntry.main.temp}C"
    }

    private fun ViewHolder.updateImage() {
        //http://openweathermap.org/img/wn/10d@2x.png
        GlideApp.with(this.containerView)
                .load("http://openweathermap.org/img/wn/${weatherEntry.weather[0].icon}@2x.png")
                .into(imageView_condition_icon)
    }

    override fun getLayout(): Int = itemLayout
}