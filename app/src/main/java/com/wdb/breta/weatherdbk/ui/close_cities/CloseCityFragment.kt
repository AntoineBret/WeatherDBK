package com.wdb.breta.weatherdbk.ui.close_cities

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hadeso.moviedb.di.Injectable
import com.wdb.breta.weatherdbk.R
import kotlinx.android.synthetic.main.close_city_fragment.*
import javax.inject.Inject

class CloseCityFragment : Fragment(), Injectable {

  companion object {
    fun newInstance(): Fragment {
      return CloseCityFragment()
    }
  }

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var viewModel: CloseCityViewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.close_city_fragment, container, false)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(CloseCityViewModel::class.java)

    viewModel.cityWeatherLiveData().observe(this, Observer { cityWeather ->
      cityWeather?.let {
        showWeatherData(it)
      }
    })

    viewModel.loadingLiveData().observe(this, Observer { isLoading ->
      isLoading?.let {
        showLoadingDialog(it)
      }
    })

    context?.let {
      if (ContextCompat.checkSelfPermission(
          it,
          Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
      ) {
        ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 0)
      } else {
        if (savedInstanceState == null) {
          viewModel.init()
        }
      }
    }
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    when (requestCode) {
      0 -> {
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
          viewModel.init()
        } else {

        }
        return
      }
      else -> {

      }
    }
  }

  private fun showWeatherData(closeCityViewData: CloseCityViewData) {
    cityName.text = closeCityViewData.name
    cityTemp.text = closeCityViewData.temp + "°"
    citySunrise.text = closeCityViewData.sunrise
    citySunset.text = closeCityViewData.sunset
    cityHumidity.text = closeCityViewData.humidity
    cityWind.text = closeCityViewData.wind + "km/h"
  }

  private fun showLoadingDialog(isLoading: Boolean) {
    if (isLoading) {
      progressBar.visibility = View.VISIBLE
      group.visibility = View.GONE
    } else {
      progressBar.visibility = View.GONE
      group.visibility = View.VISIBLE
    }
  }
}
