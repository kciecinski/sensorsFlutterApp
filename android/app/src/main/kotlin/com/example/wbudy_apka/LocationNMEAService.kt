package com.example.wbudy_apka

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import io.flutter.Log

class LocationNMEAService : Service(), NMEAListener.NMEAEventListener {
    private lateinit var nmeaLisener: NMEAListener
    override fun onCreate() {
        super.onCreate()
        nmeaLisener = NMEAListener(this@LocationNMEAService)
        nmeaLisener.setNMEAListener(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!nmeaLisener.isListening) nmeaLisener.startListen()
        notification()
        return Service.START_NOT_STICKY
    }

    override fun onDestroy() {
        if (nmeaLisener.isListening) nmeaLisener.stopListen()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun notification() { // create the notification
        val m_notificationBuilder: Notification.Builder = Notification.Builder(this)
                .setContentTitle("LocationNMEAService")
                .setContentText("")
                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
        // create the pending intent and add to the notification
        val intent = Intent(this, LocationNMEAService::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        m_notificationBuilder.setContentIntent(pendingIntent)
        // send the notification
        val m_notificationManager: NotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        m_notificationManager.notify(NOTIFICATION_ID, m_notificationBuilder.build())
        startForeground(NOTIFICATION_ID, m_notificationBuilder.build())
    }


    override fun onNMEAListener(param1Long: Long, param1String: String?) {
        Log.i("NMEA", param1String.toString())
    }

    companion object {
        private const val NOTIFICATION_ID = 101
    }
}