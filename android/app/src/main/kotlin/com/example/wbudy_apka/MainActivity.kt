package com.example.wbudy_apka

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import androidx.annotation.NonNull
import com.example.wbudy_apka.location.Position
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity: FlutterActivity() {
    private val SENSORS_CHANNEL = "samples.flutter.dev/sensors"
    private val GPS_CHANNEL = "samples.flutter.dev/gps"
    
    val gyroListener = PositionAndMotionListener();
    val accListener = PositionAndMotionListener();
    val magneticListener = PositionAndMotionListener();
    val lightListener = EnvirementalListener();

    private lateinit var locationNMEAServiceIntent: Intent
    private lateinit var locationService: LocationService
    private var locationServiceConnected: Boolean = false
    private val locationServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder: LocationService.LocationServiceBinder = service as LocationService.LocationServiceBinder
            locationService = binder.service
            locationServiceConnected = true
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            locationServiceConnected = false
        }
    }

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, SENSORS_CHANNEL).setMethodCallHandler {
            call, result ->
            when (call.method) {
              "getGyroscopeValues" -> checkResultFor(getValuesFor(Sensor.TYPE_GYROSCOPE, gyroListener), result)
              "getAccelerometrValues" -> checkResultFor(getValuesFor(Sensor.TYPE_ACCELEROMETER, accListener), result)
              "getMagneticFieldValues" -> checkResultFor(getValuesFor(Sensor.TYPE_MAGNETIC_FIELD, magneticListener), result)
              "getLightValues" -> checkResultFor(getValuesFor(Sensor.TYPE_LIGHT, lightListener), result)
              else -> {
                result.notImplemented();
              }
            }
        }
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, GPS_CHANNEL).setMethodCallHandler {
            call, result ->
            when(call.method) {
                "startGps" -> startGpsService()
                "getPosition" -> getPosition(result)
                else -> {
                    result.notImplemented();
                }
            }
        }
    }

    private fun startGpsService() {
        locationNMEAServiceIntent = Intent(this,LocationService::class.java)
        startService(locationNMEAServiceIntent)
        bindService(locationNMEAServiceIntent,locationServiceConnection,Context.BIND_AUTO_CREATE)
    }

    private fun isPositionAvailable():HashMap<String,String> {
        var returns: HashMap<String,String> = HashMap<String,String> ()
        returns.put("isPositionAvailable",locationServiceConnected.toString())
        return returns
    }

    private fun getPosition(result:MethodChannel.Result) {
        if (!locationServiceConnected){
          result.success(isPositionAvailable())
        } else {
          var position = locationService.getLastPosition()
          var positionHashMap: HashMap<String,String> = HashMap<String,String>()
          positionHashMap.put("longtitude", position.longitude.longitude.toString())
          positionHashMap.put("latitude", position.latitude.latitude.toString())
          positionHashMap.put("time",position.datetime.toString())
          positionHashMap.put("isPositionAvailable",locationServiceConnected.toString())
          if(position.availableAltitude)
              positionHashMap.put("altitude",position.altitude.toString())
          result.success(positionHashMap)
        }

    } 

    private fun checkResultFor(values: HashMap<String,Double>, result:MethodChannel.Result): Void? {
      if (values != {}) {
        result.success(values);
      } else {
        result.error("UNAVAILABLE", "Sensor not available.", null);
      }
      return null;
    }

    private fun getValuesFor(sensorType: Int,listener: BasicSensoreListener ): HashMap<String,Double> {
        val sensorManager =  getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor: Sensor = sensorManager.getDefaultSensor(sensorType)
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        return listener.values;
      }
}

class PositionAndMotionListener : BasicSensoreListener {

    override val values:HashMap<String,Double> = HashMap<String,Double>() 

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
  
    }
    override fun onSensorChanged(event: SensorEvent?) {
        val sensorName: String = event?.sensor!!.getName();
        values.put("x", event.values[0].toDouble());
        values.put("y", event.values[1].toDouble());
        values.put("z", event.values[2].toDouble());
    }   
}

class EnvirementalListener : BasicSensoreListener {

  override val values:HashMap<String,Double> = HashMap<String,Double>() 

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
  override fun onSensorChanged(event: SensorEvent?) {
      val sensorName: String = event?.sensor!!.getName();
      values.put("value", event.values[0].toDouble());
  }
}

interface BasicSensoreListener: SensorEventListener {
  val values:HashMap<String,Double>; 
}