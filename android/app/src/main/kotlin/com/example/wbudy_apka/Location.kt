package com.example.wbudy_apka

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log
import com.example.wbudy_apka.location.*

class Location(private var context: Context): NMEAListener.NMEAEventListener, AndroidLocationListener.AndroidLocationEventListener, PositionListener {
    private var androidLocationListener: AndroidLocationListener
    private var nmeaListener: NMEAListener
    private var nmeaDecoder: NMEADecoder = NMEADecoder();
    private var locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    init {
        nmeaDecoder.addPositionListener(this);
        androidLocationListener = AndroidLocationListener(context);
        androidLocationListener.setAndroidLocationListener(this);
        nmeaListener = NMEAListener(context);
        nmeaListener.setNMEAListener(this);
    }
    fun start() {
        if (!nmeaListener.isListening) nmeaListener.startListen()
        if (!androidLocationListener.isListening) androidLocationListener.startListen()
    }
    fun stop() {
        if (nmeaListener.isListening) nmeaListener.stopListen()
        if (!androidLocationListener.isListening) androidLocationListener.stopListen()
    }
    private var _lastPosition: Position? = null
    var lastPosition: Position
        @SuppressLint("MissingPermission")
        get() {
            if(_lastPosition == null) {
                var location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if(location != null) {
                    _lastPosition = Position.createFromAndroidLocation(location)
                } else {
                    val providers = locationManager.allProviders
                    var usedProvider: String = ""
                    for (provider in providers) {
                        val x: Location? = locationManager.getLastKnownLocation(provider)
                        if(x!=null) {
                            location = x
                            usedProvider = provider.toString()
                            break
                        }
                    }
                    if(location != null) {
                        _lastPosition = Position.createFromAndroidLocation(location)
                        print("Provider: "+ usedProvider)
                    } else {
                        _lastPosition = Position(Latitude(0.0),Longitude(0.0),System.currentTimeMillis())
                    }
                }
            }
            return _lastPosition!!
        }
        private set(value) {
            _lastPosition = value
            this.positionListener?.newPosition(value)
        }

    private var positionListener: PositionListener? = null
    fun setPositionListener(positionListener: PositionListener?) {
        this.positionListener = positionListener;
        this.positionListener?.newPosition(lastPosition);
    }
    private var _isNMEAWorks: Boolean = false;
    var isNMEAWorks: Boolean
        get() { return _isNMEAWorks }
        private set(value) { _isNMEAWorks = value }
    fun resetIsNMEAWorks() {
        isNMEAWorks = false
    }
    /**
     * Handler for incomming location from system
     */
    override fun onAndroidLocation(location: Location) {
        if(!isNMEAWorks)
            lastPosition = Position.createFromAndroidLocation(location)
    }
    /**
     * Handler for incomming locatin from nmea decoder
     */
    override fun newPosition(position: Position) {
        isNMEAWorks = true
        lastPosition = position
    }
    /**
     * Handler for incomming string with NMEA
     */
    override fun onNMEA(param1Long: Long, param1String: String?){
        param1String?.let { nmeaDecoder.decodeString(it) }
    }
}