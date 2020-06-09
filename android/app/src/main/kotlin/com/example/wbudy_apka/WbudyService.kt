package com.example.wbudy_apka

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Binder
import android.os.IBinder
import com.example.wbudy_apka.location.*
import com.example.wbudy_apka.model.LatLong
import com.example.wbudy_apka.sensors.EnvirementalListener
import com.example.wbudy_apka.sensors.PositionAndMotionListener
import io.flutter.Log

class WbudyService : Service(), NMEAListener.NMEAEventListener, AndroidLocationListener.AndroidLocationEventListener, PositionListener {
    companion object {
        private const val NOTIFICATION_ID = 101
        private const val LOG_TAG = "WbudyService"
    }
    private lateinit var binder: Binder
    //For location
    private lateinit var androidLocationListener: AndroidLocationListener
    private lateinit var locationManager: LocationManager
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
                    throw error("Location not available")
                }
            }
        }
        return _lastPosition!!
    }
    private set(value) {
        _lastPosition = value
        childState.currentPosition = LatLong(value.latitude.latitude,value.longitude.longitude)
    }
    private lateinit var nmeaListener: NMEAListener
    private lateinit var nmeaDecoder: NMEADecoder
    private var nmeaCommunicationWorks: Boolean = false;
    //For sensors
    private lateinit var sensorManager: SensorManager
    private val _gyroListener = PositionAndMotionListener()
    private val _accListener = PositionAndMotionListener()
    private val _magneticListener = PositionAndMotionListener()
    private val _lightListener = EnvirementalListener()
    val gyroListener: PositionAndMotionListener get() { return _gyroListener }
    val accListener: PositionAndMotionListener get() { return _accListener }
    val magneticListener: PositionAndMotionListener get() { return _magneticListener }
    val lightListener: EnvirementalListener get() { return _lightListener }

    //For calculate child state
    private lateinit var childState: ChildState;

    override fun onCreate() {
        super.onCreate()
        binder = WbudyServiceBinder(this)
        //For calculate child state
        childState = ChildState(this@WbudyService);
        //For location
        locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        nmeaDecoder = NMEADecoder()
        nmeaDecoder.addPositionListener(this)
        androidLocationListener = AndroidLocationListener(this@WbudyService)
        androidLocationListener.setAndroidLocationListener(this)
        nmeaListener = NMEAListener(this@WbudyService)
        nmeaListener.setNMEAListener(this)
        //For sensors
        sensorManager =  getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(accListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(gyroListener, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(magneticListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(lightListener, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //For location
        if (!nmeaListener.isListening) nmeaListener.startListen()
        if (!androidLocationListener.isListening) androidLocationListener.startListen()

        notification()
        return Service.START_NOT_STICKY
    }

    override fun onDestroy() {
        //For location
        if (nmeaListener.isListening) nmeaListener.stopListen()
        if (!androidLocationListener.isListening) androidLocationListener.stopListen()
        sensorManager.unregisterListener(accListener)
        sensorManager.unregisterListener(gyroListener)
        sensorManager.unregisterListener(magneticListener)
        sensorManager.unregisterListener(lightListener)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        //For location
        nmeaCommunicationWorks = false;

        Log.i(LOG_TAG,"onBind")
        return binder
    }

    private fun notification() { // create the notification
        val m_notificationBuilder: Notification.Builder = Notification.Builder(this)
                .setContentTitle("Wbudy Service")
                .setContentText("")
                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
        // create the pending intent and add to the notification
        val intent = Intent(this, WbudyService::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        m_notificationBuilder.setContentIntent(pendingIntent)
        // send the notification
        val m_notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        m_notificationManager.notify(NOTIFICATION_ID, m_notificationBuilder.build())
        startForeground(NOTIFICATION_ID, m_notificationBuilder.build())
    }

    /**
     * Handler for incomming string with NMEA
     */
    override fun onNMEA(param1Long: Long, param1String: String?){
        param1String?.let { nmeaDecoder.decodeString(it) }
    }

    /**
     * Handler for incomming location from system
     */
    override fun onAndroidLocation(location: Location) {
        if(!nmeaCommunicationWorks)
            lastPosition = Position.createFromAndroidLocation(location)
    }

    /**
     * Handler for incomming locatin from nmea decoder
     */
    override fun newPosition(position: Position) {
        nmeaCommunicationWorks = true
        lastPosition = position
    }

    fun isNMEAWorks(): Boolean {
        return this.nmeaCommunicationWorks;
    }

    fun getChildState(): ChildState {
        return childState;
    }
    
    class WbudyServiceBinder(var wbudyService: WbudyService) : Binder() {
        val service: WbudyService
            get() = this.wbudyService
    }
}