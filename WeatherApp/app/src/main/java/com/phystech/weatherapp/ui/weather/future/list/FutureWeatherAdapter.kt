package com.phystech.weatherapp.ui.weather.future.list

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.phystech.weatherapp.R
import com.phystech.weatherapp.data.network.response.Lists
import com.phystech.weatherapp.internal.glide.GlideApp
import kotlinx.android.synthetic.main.item_future_weather.*
import org.w3c.dom.Text
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class FutureWeatherAdapter(
    val weatherEntry : List<Lists>
) : RecyclerView.Adapter<FutureWeatherAdapter.ViewHolder>() {


    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val condition : TextView
        val date : TextView
        val temperature : TextView
        val weatherIcon : ImageView
        val dayTime : TextView

        init {
            condition = view.findViewById(R.id.textView_condition)
            date = view.findViewById(R.id.textView_date)
            temperature = view.findViewById(R.id.textView_temperature)
            weatherIcon = view.findViewById(R.id.imageView_condition_icon)
            dayTime = view.findViewById(R.id.textView_dayTime)
        }
    }


    override fun getItemCount(): Int = weatherEntry.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.temperature.text = weatherEntry[position].main.temp.toString()
        holder.condition.text = weatherEntry[position].weather[0].description.toString()

        GlideApp.with(holder.itemView)
            .load("http://openweathermap.org/img/wn/${weatherEntry[position].weather[0].icon}@2x.png")
            .into(holder.weatherIcon)

        val tmp = weatherEntry[position].dtTxt.split(" ")
        val dtFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
        holder.date.text = tmp[0].format(dtFormatter)
        holder.dayTime.text = tmp[1].substring(0,5)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_future_weather, parent, false)
       return ViewHolder(view)
    }


}