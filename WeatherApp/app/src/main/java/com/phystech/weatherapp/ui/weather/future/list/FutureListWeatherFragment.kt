package com.phystech.weatherapp.ui.weather.future.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.phystech.weatherapp.data.network.response.Lists
import com.phystech.weatherapp.databinding.FutureListWeatherFragmentBinding
import com.phystech.weatherapp.ui.base.ScopeFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.future_list_weather_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class FutureListWeatherFragment : ScopeFragment(),KodeinAware {

    override val kodein: Kodein by kodein()
    private val viewModelFactory : FutureListWeatherViewModelFactory by instance()

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel : FutureListWeatherViewModel


    private var _binding : FutureListWeatherFragmentBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FutureListWeatherFragmentBinding.inflate(layoutInflater, container, false)
        recyclerView = binding.recyclerView
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory)
                .get(FutureListWeatherViewModel::class.java)
        bindUI()
    }

    private fun bindUI() = launch{
        val futureWeatherEntry = viewModel.futureWeather.await()
        val weatherLocation = viewModel.location.await()

        weatherLocation.observe(viewLifecycleOwner , Observer { location ->
            if(location == null) return@Observer
            updateLocation(location.name)
        })

        futureWeatherEntry.observe(viewLifecycleOwner, Observer { weatherEntry ->

            if(weatherEntry == null) return@Observer

            binding.groupLoadingFuture.visibility = View.GONE

            updateDateToNextWeek()

            recyclerView.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.adapter = FutureWeatherAdapter(weatherEntry.list)

            }

        })

    }

    private fun List<Lists>.toFutureWeatherItems() : List<FutureWeatherItem> {
        return this.map {
            FutureWeatherItem(it)
        }
    }

    private fun updateLocation(location : String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = location
    }

    private fun updateDateToNextWeek() {
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "Next Week"
    }

}