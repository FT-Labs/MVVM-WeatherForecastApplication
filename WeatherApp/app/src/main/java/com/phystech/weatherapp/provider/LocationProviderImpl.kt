package com.phystech.weatherapp.provider

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.phystech.weatherapp.data.db.entity.Location
import com.phystech.weatherapp.util.asDeferred
import kotlinx.coroutines.Deferred
import java.lang.Exception
import kotlin.math.abs

const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"


class LocationProviderImpl(
        context: Context,
        private val fusedLocationProviderClient: FusedLocationProviderClient
        ) :PreferenceProvider(context), LocationProvider {

    private val appContext = context.applicationContext

    override fun getContext(): Context {
        return appContext
    }

    override suspend fun isLocationChanged(lastWeatherLocation: Location): Boolean {
        val deviceLocationChanged = try {
            hasLocationChanged(lastWeatherLocation)
        } catch (e : Exception) {
            false
        }


        return deviceLocationChanged || hasCustomLocationChanged(lastWeatherLocation)
    }

    private suspend fun hasLocationChanged(lastWeatherLocation: Location) : Boolean
    {
        if(!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocation().await()
                ?: return false

        val comparisonThreshold = 0.03
        return abs(deviceLocation.latitude - lastWeatherLocation.lat) > comparisonThreshold &&
                abs(deviceLocation.longitude - lastWeatherLocation.lon) > comparisonThreshold

    }

    private fun hasLocationPermission() : Boolean {
        return ContextCompat.checkSelfPermission(appContext,
            android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    private fun hasCustomLocationChanged(lastWeatherLocation: Location): Boolean {
        if (!isUsingDeviceLocation()) {
            val customLocationName = getCustomLocationName()
            return customLocationName != lastWeatherLocation.name
        }
        return false
    }

    private fun getCustomLocationName() : String? {
        return preferences.getString(CUSTOM_LOCATION, null)
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation() : Deferred<android.location.Location?>
    {
        return if (hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw Exception()
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)

    }

    override suspend fun getPreferredLocationString(): String {
        if (isUsingDeviceLocation())
        {
            try{

                val deviceLocation = getLastDeviceLocation().await() ?: return "${getCustomLocationName()}"
            }
            catch (e : Exception)
            {
                return "${getCustomLocationName()}"
            }
        }
        return "${getCustomLocationName()}"
    }
}