package com.phystech.weatherapp.ui.weather.current

import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.phystech.weatherapp.data.network.ConnectivityInterceptorImpl
import com.phystech.weatherapp.data.network.WeatherApiService
import com.phystech.weatherapp.data.network.WeatherNetworkDataSourceImpl
import com.phystech.weatherapp.databinding.CurrentWeatherFragmentBinding
import com.phystech.weatherapp.provider.PreferenceProvider
import com.phystech.weatherapp.ui.base.ScopeFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

private const val TAG = "CurrentWeatherFragment"

class CurrentWeatherFragment : ScopeFragment(), KodeinAware{

    override val kodein: Kodein by kodein()
    private val viewModelFactory : CurrentWeatherViewModelFactory by instance()
    private lateinit var preferences : SharedPreferences

    private var _binding : CurrentWeatherFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CurrentWeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CurrentWeatherFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(CurrentWeatherViewModel::class.java)
        preferences = PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)
        bindUI()
    }



    private fun bindUI() = launch {
        val currentWeather = viewModel.weather.await()
        val currentLocation = viewModel.location.await()

        currentLocation.observe(viewLifecycleOwner, Observer { location->
            if(location == null) return@Observer
            updateLocation(location.name)


            if(location.name != preferences.getString("CUSTOM_LOCATION",null))
            {
                Toast.makeText(context, "City name: ${preferences.getString("CUSTOM_LOCATION",null)} is not valid.", Toast.LENGTH_LONG).show()
            }

        })

        currentWeather.observe(viewLifecycleOwner, Observer {
            if (it == null)
                return@Observer


            binding.groupLoading.visibility = View.GONE
            updateData(it.temperature, it.feelslike, it.precip, it.visibility,it.windDir, it.windSpeed)

            Log.d(TAG, it.weatherIcons[0])

            Glide.with(this@CurrentWeatherFragment)
                    .load(it.weatherIcons[0])
                    .into(binding.weatherImageIcon)
        })
    }

    private fun updateData(
            temperature: Double, feelsLikeTemperature: Double,
            precipitationVolume: Double,
            visibilityDistance: Double,
            windDirection: String, windSpeed: Double)
    {
        updateTemperatures(temperature, feelsLikeTemperature)
        updatePrecipitation(precipitationVolume)
        updateVisibility(visibilityDistance)
        updateWind(windDirection, windSpeed)
        updateToToday()

    }

    private fun updateTemperatures(temperature : Double, feelsLikeTemperature : Double)
    {
        binding.textViewTemperature.text = "$temperature C"
        binding.textViewFeelsLike.text = "Feels like: $feelsLikeTemperature C"
    }


    private fun updatePrecipitation(precipitationVolume : Double)
    {
        binding.textViewPrecipitation.text = "Precipitation: $precipitationVolume mm"
    }

    private fun updateLocation(location : String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateVisibility(visibilityDistance : Double)
    {
        binding.textViewVisibility.text = "Visibility: $visibilityDistance km"
    }

    private fun updateWind(windDirection : String, windSpeed : Double)
    {
        binding.textViewWind.text = "Direction: $windDirection , Wind Speed: $windSpeed km"
    }

    private fun updateToToday()
    {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Today"
    }


}