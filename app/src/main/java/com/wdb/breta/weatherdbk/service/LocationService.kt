package com.wdb.breta.weatherdbk.service

import android.annotation.SuppressLint
import android.app.Application
import android.location.Location
import com.google.android.gms.common.internal.Constants
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import io.reactivex.Single
import io.reactivex.SingleEmitter
import javax.inject.Inject

class LocationService @Inject constructor(private val application: Application) {

  private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)

  @SuppressLint("MissingPermission")
  fun getLocation(): Single<Location> {
    return Single.create<Location> { singleEmitter ->
      fusedLocationClient.lastLocation.addOnSuccessListener {
        if (it == null) {
          getLocationUpdates(singleEmitter)
        } else {
          singleEmitter.onSuccess(it)
        }
      }
      fusedLocationClient.lastLocation.addOnFailureListener {
        singleEmitter.onError(it)
      }
    }
  }

  @SuppressLint("MissingPermission")
  fun getLocationUpdates(singleEmitter: SingleEmitter<Location>) {

    val mLocationRequest = LocationRequest()
    mLocationRequest.interval = 120000
    mLocationRequest.fastestInterval = 60000
    mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

    fusedLocationClient.requestLocationUpdates(mLocationRequest, object : LocationCallback() {
      override fun onLocationResult(locationResult: LocationResult?) {
        super.onLocationResult(locationResult)
        locationResult?.let {
          singleEmitter.onSuccess(it.lastLocation)
        } ?: singleEmitter.onError(Exception())

        fusedLocationClient.removeLocationUpdates(this)
      }
    }, null)
  }
}

