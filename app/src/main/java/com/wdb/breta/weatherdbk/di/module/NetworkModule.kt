package com.wdb.breta.weatherdbk.di.module

import com.squareup.moshi.Moshi
import com.wdb.breta.weatherdbk.service.RemotePictureService
import com.wdb.breta.weatherdbk.service.RemoteWeatherService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

const val TIMEOUT_SECONDS = 20

@Module
class NetworkModule {

  @Provides
  @Singleton
  fun provideOkHttpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY

    return OkHttpClient.Builder()
      .connectTimeout(TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
      .readTimeout(TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
      .writeTimeout(TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
      .addInterceptor(logging)
      .build()
  }

  @Provides
  @Singleton
  @Named("WEATHER_API")
  fun provideWeatherService(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
    Retrofit.Builder()
      .baseUrl(NetworkConfig.BASE_API_WEATHER)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .client(okHttpClient)
      .build()

  @Provides
  @Singleton
  @Named("PICTURE_API")
  fun provideUnsplashService(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
    Retrofit.Builder()
      .baseUrl(NetworkConfig.BASE_API_UNSPLASH)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .client(okHttpClient)
      .build()

  @Provides
  @Singleton
  fun provideWeatherBaseUrl(@Named("WEATHER_API") retrofit: Retrofit): RemoteWeatherService =
    retrofit.create(RemoteWeatherService::class.java)

  @Singleton
  @Provides
  fun provideUnsplashBaseUrl(@Named("PICTURE_API") retrofit: Retrofit): RemotePictureService =
    retrofit.create(RemotePictureService::class.java)
}

