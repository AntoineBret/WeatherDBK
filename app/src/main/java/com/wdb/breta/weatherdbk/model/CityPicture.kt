package com.wdb.breta.weatherdbk.model

data class CityPicture(
  val cityWeather: CityWeather,
  val query: String,
  val orientation: String,
  val urls: Urls
)
