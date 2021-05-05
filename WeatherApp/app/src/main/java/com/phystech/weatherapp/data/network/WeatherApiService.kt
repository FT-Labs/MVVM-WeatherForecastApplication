package com.phystech.weatherapp.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.phystech.weatherapp.data.network.interfaces.ConnectivityInterceptor
import com.phystech.weatherapp.data.network.response.CurrentWeatherResponse
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val API_KEY = "58200945bade0fd2d2b1986d8746f814"


//http://api.weatherstack.com/current?access_key=58200945bade0fd2d2b1986d8746f814&query=New%20York

interface WeatherApiService {

    @GET("current")
    fun getCurrentWeather(
    @Query("query") location : String,
    @Query("lang") languageCode : String = "en") : Deferred<CurrentWeatherResponse>

    companion object {
        operator fun invoke(
                connectivityInterceptor: ConnectivityInterceptor
        ) : WeatherApiService {
            val requestInterceptor = Interceptor{chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("access_key", API_KEY)
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val httpClient : OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(connectivityInterceptor)
                .addInterceptor(requestInterceptor)
                .build()

            return Retrofit.Builder()
                .client(httpClient)
                .baseUrl("http://api.weatherstack.com/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService::class.java)
        }
    }



}