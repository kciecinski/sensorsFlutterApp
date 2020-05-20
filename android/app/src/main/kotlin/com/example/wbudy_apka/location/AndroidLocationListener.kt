package com.example.wbudy_apka.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle

class AndroidLocationListener(paramContext: Context) : LocationListener {
    private var androidLocationListener: AndroidLocationEventListener? = null

    fun setAndroidLocationListener(listener: AndroidLocationEventListener) {
        androidLocationListener = listener
    }

    var isListening = false
        private set
    private lateinit var locationManager: LocationManager

    init {
        locationManager = paramContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }


    @SuppressLint("MissingPermission")
    fun startListen() {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0.0f, this)
        isListening = true
    }

    fun stopListen() {
        locationManager.removeUpdates(this)
        isListening = false
    }

    override fun onLocationChanged(location: Location?) {
        if(location == null)
            return
        if(androidLocationListener == null)
            return
        androidLocationListener!!.onAndroidLocation(location)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String?) {}

    override fun onProviderDisabled(provider: String?) {}

    interface AndroidLocationEventListener {
        fun onAndroidLocation(location: Location)
    }
}