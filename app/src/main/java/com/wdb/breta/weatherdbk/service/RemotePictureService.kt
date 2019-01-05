package com.wdb.breta.weatherdbk.service

import com.wdb.breta.weatherdbk.model.CityPicture
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RemotePictureService {

  @GET("/photos/random")
  abstract fun getBackgroundScreen(
    @Query("query") query: String,
    @Query("orientation") orientation: String,
    @Query("client_id") client_id: String
    ): Single<CityPicture>
}
