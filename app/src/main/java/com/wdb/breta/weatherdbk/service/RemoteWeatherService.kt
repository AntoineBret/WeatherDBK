package com.wdb.breta.weatherdbk.service

import com.wdb.breta.weatherdbk.model.CityWeather
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteWeatherService {

  @GET("weather")
  abstract fun getCityByName(
    @Query("q") city: String,
    @Query("mode") format: String,
    @Query("units") units: String,
    @Query("APPID") api_key: String
  ): Observable<Response<CityWeather>>

  @GET("weather")
  abstract fun getCityByLocation(
    @Query("lat") latitude: Double,
    @Query("lon") longitude: Double,
    @Query("units") units: String,
    @Query("APPID") apiKey: String
  ): Single<CityWeather>

  @GET("/search/photos")
  abstract fun getBackgroundScreen(
    @Query("client_id") client_id: String,
    @Query("query") query: String
    ): Single<CityWeather>
}
