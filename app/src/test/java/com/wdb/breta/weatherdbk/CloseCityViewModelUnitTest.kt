package com.wdb.breta.weatherdbk

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.location.Location
import com.wdb.breta.weatherdbk.model.*
import com.wdb.breta.weatherdbk.service.LocationService
import com.wdb.breta.weatherdbk.service.RemotePictureService
import com.wdb.breta.weatherdbk.service.RemoteWeatherService
import com.wdb.breta.weatherdbk.ui.close_cities.CloseCityViewModel
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit

class CloseCityViewModelUnitTest {

  @Rule
  @JvmField
  val rule = MockitoJUnit.rule()!!

  @get:Rule
  var testRule: TestRule = InstantTaskExecutorRule()

  //Mock
  private val locationService = Mockito.mock(LocationService::class.java)
  private val apiService = Mockito.mock(RemoteWeatherService::class.java)
  private val unsplashService = Mockito.mock(RemotePictureService::class.java)

  private val cityWeatherMock = CityWeather(
    "Test",
    Mockito.mock(Coord::class.java),
    Mockito.mock(Sys::class.java),
    listOf(Mockito.mock(Weather::class.java)),
    Mockito.mock(Main::class.java),
    Mockito.mock(Wind::class.java),
    0,
    0,
    0
  )

  //Class under test
  private val viewModel = Mockito.spy(CloseCityViewModel(locationService, apiService, unsplashService))

  @Before
  fun setup() {
    RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
    RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
    RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
  }

  @Test
  fun testInit() {
    //Given
    val location = Mockito.mock(Location::class.java)
    Mockito.`when`(locationService.getLocation()).thenReturn(Single.just(location))
    Mockito.`when`(
      apiService.getCityByLocation(
        Mockito.anyDouble(),
        Mockito.anyDouble(),
        Mockito.anyString(),
        Mockito.anyString()
      )
    ).thenReturn(Single.just(cityWeatherMock))

    //When
    viewModel.init()

    //Then
    Mockito.verify(locationService).getLocation()
    Mockito.verify(apiService).getCityByLocation(
      Mockito.anyDouble(),
      Mockito.anyDouble(),
      Mockito.anyString(),
      Mockito.anyString()
    )
  }
}
