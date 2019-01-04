package com.wdb.breta.weatherdbk

import android.app.Activity
import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.wdb.breta.weatherdbk.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class WeatherDBKApp : Application() , HasActivityInjector {

  @Inject
  lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }

    AppInjector.init(this)
    AndroidThreeTen.init(this)
  }

  override fun activityInjector(): DispatchingAndroidInjector<Activity> {
    return dispatchingAndroidInjector
  }
}
