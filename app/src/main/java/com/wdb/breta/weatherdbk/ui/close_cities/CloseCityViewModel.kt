package com.wdb.breta.weatherdbk.ui.close_cities

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.location.Location
import com.wdb.breta.weatherdbk.BuildConfig
import com.wdb.breta.weatherdbk.model.CityPicture
import com.wdb.breta.weatherdbk.model.CityWeather
import com.wdb.breta.weatherdbk.model.Urls
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
import retrofit2.http.Url
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
    disposable.add(getLocation()
      .subscribeOn(Schedulers.io())
      .observeOn(Schedulers.io())
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

  private fun getClosestCityPicture() {
    loadingLiveData.value = true
    disposable.add(getCityByName(CityPicture("Sydney", "portrait"))
      .subscribeOn(Schedulers.io())
      .observeOn(Schedulers.io())
      .flatMap { getCityByName(it) }
      .map { modeltoViewPictureData(it) }
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({ cityPicture ->
        Timber.d(cityPicture.toString())
        loadingLiveData.value = false
        cityPictureLiveData.value = cityPicture
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

  private fun getCityByName(cityPicture: CityPicture): Single<CityPicture> {
    return unsplashApiService.getBackgroundScreen(
      cityPicture.query,
      cityPicture.orientation,
      BuildConfig.UNSPLASH_CLIENT_ID
    )
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
