package com.phystech.weatherapp.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.phystech.weatherapp.data.network.interfaces.ConnectivityInterceptor
import com.phystech.weatherapp.data.network.response.FutureWeatherResponse
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.openweathermap.org/data/2.5/forecast?q=Istanbul&appid=0c1b7d1799d1e703e497f3bffd025215

private const val API_KEY = "0c1b7d1799d1e703e497f3bffd025215"

interface FutureWeatherApiService {
    @GET("forecast")
    fun getFutureWeather(
            @Query("q") location : String
    ) : Deferred<FutureWeatherResponse>

    companion object {
        operator fun invoke(
                connectivityInterceptor: ConnectivityInterceptor
        ) : FutureWeatherApiService {
            val requestInterceptor = Interceptor {chain ->

                val url = chain.request()
                        .url()
                        .newBuilder()
                        .addQueryParameter("units","metric")
                        .addQueryParameter("appid", API_KEY)
                        .build()

                val request = chain.request()
                        .newBuilder()
                        .url(url)
                        .build()
                return@Interceptor chain.proceed(request) }

                val httpClient : OkHttpClient = OkHttpClient.Builder()
                        .addInterceptor(connectivityInterceptor)
                        .addInterceptor(requestInterceptor)
                        .build()

            return Retrofit.Builder()
                    .client(httpClient)
                    .baseUrl("https://api.openweathermap.org/data/2.5/")
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(FutureWeatherApiService::class.java)
        }
    }
}