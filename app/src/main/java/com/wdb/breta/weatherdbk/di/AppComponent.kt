package com.wdb.breta.weatherdbk.di

import android.app.Application
import com.wdb.breta.weatherdbk.WeatherDBKApp
import com.wdb.breta.weatherdbk.di.module.AppModule
import com.wdb.breta.weatherdbk.di.module.MainActivityModule
import com.wdb.breta.weatherdbk.di.module.NetworkModule
import com.wdb.breta.weatherdbk.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
  modules = [AndroidInjectionModule::class, AppModule::class, NetworkModule::class, ViewModelModule::class, MainActivityModule::class]
)
interface AppComponent {
  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: Application): Builder

    fun build(): AppComponent
  }

  fun inject(weatherDBKApp: WeatherDBKApp)
}
