package com.phystech.weatherapp

import android.app.Application
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import com.phystech.weatherapp.data.db.ForecastWeatherDatabase
import com.phystech.weatherapp.data.network.*
import com.phystech.weatherapp.data.network.interfaces.ConnectivityInterceptor
import com.phystech.weatherapp.data.network.interfaces.FutureWeatherDataSource
import com.phystech.weatherapp.data.network.interfaces.WeatherNetworkDataSource
import com.phystech.weatherapp.data.repository.ForecastRepository
import com.phystech.weatherapp.data.repository.ForecastRepositoryImpl
import com.phystech.weatherapp.provider.LocationProvider
import com.phystech.weatherapp.provider.LocationProviderImpl
import com.phystech.weatherapp.ui.weather.current.CurrentWeatherViewModelFactory
import com.phystech.weatherapp.ui.weather.future.list.FutureListWeatherViewModel
import com.phystech.weatherapp.ui.weather.future.list.FutureListWeatherViewModelFactory
import kotlinx.coroutines.InternalCoroutinesApi
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class ForecastApplication : Application(), KodeinAware{
    @InternalCoroutinesApi
    override val kodein: Kodein = Kodein.lazy{
        import(androidXModule(this@ForecastApplication))

        bind() from singleton { ForecastWeatherDatabase(instance()) }
        bind() from singleton { instance<ForecastWeatherDatabase>().currentWeatherDao()}
        bind() from singleton { instance<ForecastWeatherDatabase>().currentLocationDao()}
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { WeatherApiService(instance()) }
        bind() from singleton { FutureWeatherApiService(instance())}
        bind() from provider {LocationServices.getFusedLocationProviderClient(instance<Context>())}
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<FutureWeatherDataSource>() with singleton { FutureWeatherDataSourceImpl(instance())}
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind<ForecastRepository>() with singleton { ForecastRepositoryImpl(instance(), instance(), instance(), instance(), instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance()) }
        bind() from provider { FutureListWeatherViewModelFactory(instance())}
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

    }
}