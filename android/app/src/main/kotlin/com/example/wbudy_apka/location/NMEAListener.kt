package com.example.wbudy_apka.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.GpsStatus
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle

//TODO add implementation for api leval > 24
class NMEAListener(paramContext: Context) : GpsStatus.NmeaListener, LocationListener {
    private var inmeaListener: NMEAEventListener? = null
    var isListening = false
        private set
    private lateinit var locationManager: LocationManager

    override fun onLocationChanged(paramLocation: Location?) {}
    override fun onNmeaReceived(paramLong: Long, paramString: String?) {
        if (inmeaListener != null) inmeaListener!!.onNMEA(paramLong, paramString)
    }

    override fun onProviderDisabled(paramString: String?) {}
    override fun onProviderEnabled(paramString: String?) {}
    override fun onStatusChanged(paramString: String?, paramInt: Int, paramBundle: Bundle?) {}
    fun setNMEAListener(paramINMEAListener: NMEAEventListener?) {
        inmeaListener = paramINMEAListener
    }

    @SuppressLint("MissingPermission")
    fun startListen(): Boolean {
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0.0f, this)
        isListening = true
        return locationManager.addNmeaListener(this)
    }

    fun stopListen() {
        locationManager.removeNmeaListener(this)
        locationManager.removeUpdates(this)
        isListening = false
    }

    interface NMEAEventListener {
        fun onNMEA(param1Long: Long, param1String: String?)
    }

    init {
        locationManager = paramContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
}