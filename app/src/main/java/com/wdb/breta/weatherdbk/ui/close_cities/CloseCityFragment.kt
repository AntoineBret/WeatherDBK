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
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.centerInsideTransform
import com.bumptech.glide.request.RequestOptions.fitCenterTransform
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
    setHasOptionsMenu(true)
    return inflater.inflate(R.layout.close_city_fragment, container, false)
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.menu, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return (when(item.itemId) {
      R.id.action_add -> {
        true
      }
      else ->
        super.onOptionsItemSelected(item)
    })
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(CloseCityViewModel::class.java)

    //get weather data from OpenWeatherMap
    viewModel.cityWeatherLiveData().observe(this, Observer { cityWeather ->
      cityWeather?.let {
        setToolbarTitle(it)
        showWeatherData(it)
      }
    })

    //get picture from UnsplashAPI
    viewModel.cityPictureLiveData().observe(this, Observer { cityPicture ->
      cityPicture?.let {
        addBackgroundPicture(it)
      }
    })

    //set ProgressBar
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

  private fun setToolbarTitle(closeCityViewData: CloseCityViewData) {
    val actionBar = (activity as AppCompatActivity).supportActionBar
    actionBar!!.title = closeCityViewData.name
  }

  private fun showWeatherData(closeCityViewData: CloseCityViewData) {
    cityName.text = closeCityViewData.name
    cityTemp.text = closeCityViewData.temp + "°"
    citySunrise.text = closeCityViewData.sunrise
    citySunset.text = closeCityViewData.sunset
    cityHumidity.text = closeCityViewData.humidity
    cityWind.text = closeCityViewData.wind + "km/h"
  }

  private fun addBackgroundPicture(unsplashPictureViewData: UnsplashPictureViewData) {
    Glide
      .with(this)
      .load(unsplashPictureViewData.regular)
      .apply(centerInsideTransform())
      .into(background)
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
