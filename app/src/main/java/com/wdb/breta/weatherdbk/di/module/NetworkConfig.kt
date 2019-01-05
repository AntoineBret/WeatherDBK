package com.wdb.breta.weatherdbk.di.module

import com.wdb.breta.weatherdbk.BuildConfig
import okhttp3.HttpUrl

class NetworkConfig {
  companion object {
    val BASE_API_UNSPLASH =  HttpUrl.parse(BuildConfig.GATEWAY_BASE_ENDPOINT_UNSPLASH)!!
    val BASE_API_WEATHER =  HttpUrl.parse(BuildConfig.GATEWAY_BASE_ENDPOINT_WEATHER)!!
  }
}
