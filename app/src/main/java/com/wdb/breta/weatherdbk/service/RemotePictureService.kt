package com.wdb.breta.weatherdbk.service

import com.wdb.breta.weatherdbk.model.CityWeather
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RemotePictureService {

  @GET("/search/photos")
  abstract fun getBackgroundScreen(
    @Query("client_id") client_id: String,
    @Query("query") query: String
  ): Single<CityWeather>
}
