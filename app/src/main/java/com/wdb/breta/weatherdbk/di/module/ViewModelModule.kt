package com.wdb.breta.weatherdbk.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.hadeso.moviedb.di.ViewModelKey
import com.wdb.breta.weatherdbk.ui.WeatherDBKViewModelFactory
import com.wdb.breta.weatherdbk.ui.close_cities.CloseCityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

  @Binds
  @IntoMap
  @ViewModelKey(CloseCityViewModel::class)
  abstract fun bindCloseCityViewModel(closeCityViewModel: CloseCityViewModel): ViewModel

  @Binds
  abstract fun bindViewModelFactory(factory: WeatherDBKViewModelFactory): ViewModelProvider.Factory

}
