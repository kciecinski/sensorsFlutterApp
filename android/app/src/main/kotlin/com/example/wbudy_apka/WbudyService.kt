package com.example.wbudy_apka

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import io.flutter.Log

class WbudyService : Service() {
    companion object {
        private const val NOTIFICATION_ID = 101
        private const val LOG_TAG = "WbudyService"
    }
    private lateinit var binder: Binder
    //For location
    private lateinit var _location: com.example.wbudy_apka.Location
    val location: com.example.wbudy_apka.Location
    get() {return _location;}
    //For sensors
    private lateinit var _sensors: Sensors
    val sensors: Sensors
        get() {return _sensors;}
    //For calculate child state
    private lateinit var _childState: ChildState;
    val childState: ChildState
        get() { return _childState; }

    override fun onCreate() {
        super.onCreate()
        binder = WbudyServiceBinder(this)
        //For calculate child state and location
        _location = Location(applicationContext)
        _childState = ChildState(this@WbudyService)
        _childState.start()
        _location.setPositionListener(_childState)
        _location.start()
        //For sensors
        _sensors = Sensors(applicationContext)
        _sensors.start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        childState.start()
        location.start()
        sensors.start()

        notification()
        return Service.START_NOT_STICKY
    }

    override fun onDestroy() {
        childState.stop()
        location.stop()
        sensors.stop()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        location.resetIsNMEAWorks()

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
    
    class WbudyServiceBinder(var wbudyService: WbudyService) : Binder() {
        val service: WbudyService
            get() = this.wbudyService
    }
}