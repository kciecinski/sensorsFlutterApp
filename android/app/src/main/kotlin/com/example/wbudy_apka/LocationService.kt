package com.example.wbudy_apka

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Binder
import android.os.IBinder
import com.example.wbudy_apka.location.*
import io.flutter.Log


class LocationService : Service(), NMEAListener.NMEAEventListener, AndroidLocationListener.AndroidLocationEventListener, PositionListener {


    companion object {
        private const val NOTIFICATION_ID = 101
    }
    private var lastPosition: Position? = null
    private lateinit var locationManager: LocationManager
    private lateinit var binder: Binder
    private lateinit var nmeaLisener: NMEAListener
    private lateinit var androidLocationListener: AndroidLocationListener
    private lateinit var nmeaDecoder: NMEADecoder
    override fun onCreate() {
        super.onCreate()
        binder = LocationServiceBinder(this)
        locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        nmeaDecoder = NMEADecoder()
        nmeaDecoder.addPositionListener(this)
        androidLocationListener = AndroidLocationListener(this@LocationService)
        androidLocationListener.setAndroidLocationListener(this)
        nmeaLisener = NMEAListener(this@LocationService)
        nmeaLisener.setNMEAListener(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!nmeaLisener.isListening) nmeaLisener.startListen()
        if (!androidLocationListener.isListening) androidLocationListener.startListen()
        notification()
        return Service.START_NOT_STICKY
    }

    override fun onDestroy() {
        if (nmeaLisener.isListening) nmeaLisener.stopListen()
        if (!androidLocationListener.isListening) androidLocationListener.stopListen()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.i("LocationService","onBind")
        return binder
    }

    private fun notification() { // create the notification
        val m_notificationBuilder: Notification.Builder = Notification.Builder(this)
                .setContentTitle("LocationService")
                .setContentText("")
                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
        // create the pending intent and add to the notification
        val intent = Intent(this, LocationService::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        m_notificationBuilder.setContentIntent(pendingIntent)
        // send the notification
        val m_notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        m_notificationManager.notify(NOTIFICATION_ID, m_notificationBuilder.build())
        startForeground(NOTIFICATION_ID, m_notificationBuilder.build())
    }


    override fun onNMEA(param1Long: Long, param1String: String?){
        param1String?.let { nmeaDecoder.decodeString(it) }
    }


    override fun onAndroidLocation(location: Location) {
        //Toast.makeText(this@LocationService,location.toString(),Toast.LENGTH_LONG).show()
    }

    override fun newPosition(position: Position) {
        lastPosition = position
    }

    @SuppressLint("MissingPermission")
    fun getLastPosition(): Position {
        if(lastPosition == null) {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val latitude = Latitude(location.latitude)
            val longitude = Longitude(location.longitude)

            val time = location.time
            val altitude = location.altitude
            lastPosition = Position(latitude,longitude,time,altitude)
        }
        return lastPosition!!
    }
    class LocationServiceBinder(var locationService: LocationService) : Binder() {
        val service: LocationService
            get() = this.locationService
    }
}