package com.phystech.weatherapp.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.phystech.weatherapp.R
import com.phystech.weatherapp.databinding.ActivityMainBinding
import com.phystech.weatherapp.provider.LifeCycleBoundLocationManager
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.security.Permission

private const val TAG  = "MainActivity"
private const val PERMISSION_ACCESS_COARSE_LOCATION = 1

class MainActivity : AppCompatActivity(), KodeinAware{


    override val kodein by kodein()
    private val fusedLocationProviderClient : FusedLocationProviderClient by instance()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
        }
    }



    private lateinit var binding : ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        Log.d(TAG, "executed main")


        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        binding.bottomNav.setupWithNavController(navController)
        NavigationUI.setupActionBarWithNavController(this, navController)

        requestLocationPermission()

        if (hasLocationPermission())
            bindLocationManager()
        else
            requestLocationPermission()
    }


    private fun bindLocationManager()
    {
        LifeCycleBoundLocationManager(
                this,
                fusedLocationProviderClient,
                locationCallback
        )
    }

    private fun hasLocationPermission() : Boolean
    {
        return ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }



    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSION_ACCESS_COARSE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            bindLocationManager()
        else
            Toast.makeText(this, "Please set location manually in settings", Toast.LENGTH_LONG).show()
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}