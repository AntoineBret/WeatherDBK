package com.wdb.breta.weatherdbk.ui.close_cities

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.location.Location
import com.wdb.breta.weatherdbk.BuildConfig
import com.wdb.breta.weatherdbk.model.CityPicture
import com.wdb.breta.weatherdbk.model.CityWeather
import com.wdb.breta.weatherdbk.service.LocationService
import com.wdb.breta.weatherdbk.service.RemotePictureService
import com.wdb.breta.weatherdbk.service.RemoteWeatherService
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
  private val weatherApiService: RemoteWeatherService,
  private val unsplashApiService: RemotePictureService
) : ViewModel() {

  private val disposable = CompositeDisposable()
  private val cityWeatherLiveData = MutableLiveData<CloseCityViewData>()
  private val cityPictureLiveData = MutableLiveData<UnsplashPictureViewData>()
  private val loadingLiveData = MutableLiveData<Boolean>()

  //get weather data from OpenWeatherMap
  fun cityWeatherLiveData(): LiveData<CloseCityViewData> {
    return cityWeatherLiveData
  }

  //get picture from UnsplashAPI
  fun cityPictureLiveData(): LiveData<UnsplashPictureViewData> {
    return cityPictureLiveData
  }

  fun loadingLiveData(): LiveData<Boolean> {
    return loadingLiveData
  }

  fun init() {
    getClosestCityWeather()
    getClosestCityPicture()
  }

  private fun getClosestCityWeather() {
    loadingLiveData.value = true
    disposable
      //Get location from location.servicefor next call
      .add(getLocation()
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        //send request
        .flatMap { getCityByLocation(it) }
        .map { modelToViewWeatherData(it) }
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
    return weatherApiService.getCityByLocation(
      location.latitude,
      location.longitude,
      "metric",
      BuildConfig.WEATHER_API_KEY
    )
  }

  private fun getClosestCityPicture() {
    unsplashApiService.getRandomPictureByCityName(
//      cityPicture.cityWeather.name,
      "Sydney",
      "portrait",
      BuildConfig.UNSPLASH_CLIENT_ID
    )
      .subscribeOn(Schedulers.io())
      .observeOn(Schedulers.io())
      .map { modeltoViewPictureData(it) }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({ cityPicture ->
        Timber.d(cityPicture.toString())
        loadingLiveData.value = false
        cityPictureLiveData.value = cityPicture
      }, {
        Timber.e(it)
      })
  }

  private fun modelToViewWeatherData(cityWeather: CityWeather): CloseCityViewData {

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

  private fun modeltoViewPictureData(cityPicture: CityPicture): UnsplashPictureViewData {
    return UnsplashPictureViewData(
      cityPicture.urls.regular

    )
  }
}
