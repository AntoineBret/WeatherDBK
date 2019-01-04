package com.wdb.breta.weatherdbk.di.module

import com.wdb.breta.weatherdbk.ui.close_cities.CloseCityFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
  @ContributesAndroidInjector
  abstract fun contributeCloseCityFragment(): CloseCityFragment
}
