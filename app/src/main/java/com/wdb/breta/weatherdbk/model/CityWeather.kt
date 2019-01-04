package com.wdb.breta.weatherdbk.model

data class CityWeather(
  val name: String,
  val coord: Coord,
  val sys: Sys,
  val weather: List<Weather>,
  val main: Main,
  val wind: Wind,
  val dt: Long,
  val id: Long,
  val cod: Long
)
