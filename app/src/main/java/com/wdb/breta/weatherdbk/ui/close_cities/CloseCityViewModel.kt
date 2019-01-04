package com.wdb.breta.weatherdbk.ui.close_cities

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.location.Location
import com.wdb.breta.weatherdbk.BuildConfig
import com.wdb.breta.weatherdbk.model.CityWeather
import com.wdb.breta.weatherdbk.service.LocationService
import com.wdb.breta.weatherdbk.service.WeatherDBKAppService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import javax.inject.Inject

class CloseCityViewModel @Inject constructor(
  private val locationService: LocationService,
  private val apiService: WeatherDBKAppService
) : ViewModel() {

  private val disposable = CompositeDisposable()
  private val cityWeatherLiveData = MutableLiveData<CloseCityViewData>()
  private val loadingLiveData = MutableLiveData<Boolean>()

  fun cityWeatherLiveData(): LiveData<CloseCityViewData> {
    return cityWeatherLiveData
  }

  fun loadingLiveData(): LiveData<Boolean> {
    return loadingLiveData
  }

  fun init() {
    getClosestCityWeather()
  }

  private fun getClosestCityWeather() {
    loadingLiveData.value = true
    disposable.add(getLocation()
      .subscribeOn(Schedulers.io())
      .observeOn(Schedulers.io())
      .flatMap { getCityByLocation(it) }
      .map { modelToViewData(it) }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({ cityWeather ->
        Timber.d(cityWeather.toString())
        loadingLiveData.value = false
        cityWeatherLiveData.value = cityWeather
      }, {
        Timber.e(it)
      })
    )
  }

  private fun getLocation(): Single<Location> {
    return locationService.getLocation()
  }

  private fun getCityByLocation(location: Location): Single<CityWeather> {
    return apiService.getCityByLocation(location.latitude, location.longitude, "metric", BuildConfig.WEATHER_API_KEY)
  }

  private fun modelToViewData(cityWeather: CityWeather): CloseCityViewData {

    val dateTimeFormatter = DateTimeFormatter.ofPattern("hh:ss")
    val sunriseString = ZonedDateTime.ofInstant(Instant.ofEpochSecond(cityWeather.sys.sunrise), ZoneId.systemDefault())
      .format(dateTimeFormatter)
    val sunsetString = ZonedDateTime.ofInstant(Instant.ofEpochSecond(cityWeather.sys.sunset), ZoneId.systemDefault())
      .format(dateTimeFormatter)

    return CloseCityViewData(
      cityWeather.name,
      cityWeather.main.temp.toString(),
      sunriseString,
      sunsetString,
      cityWeather.main.humidity.toString(),
      cityWeather.wind.speed.toString()
    )
  }
}
