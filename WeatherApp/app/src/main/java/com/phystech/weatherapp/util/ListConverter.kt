package com.phystech.weatherapp.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class ListConverter {

    companion object {
        val gson = Gson()

        @TypeConverter
        @JvmStatic
        fun invert(data: String?): List<String>
        {
            if(data == null)
                return Collections.emptyList()

            val listType = object : TypeToken<List<String>>(){}.type
            return gson.fromJson(data, listType)
        }

        @TypeConverter
        @JvmStatic
        fun convert(objects : List<String>?) : String?
        {
            return gson.toJson(objects)
        }
    }
}