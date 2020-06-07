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
import android.util.Log
import androidx.annotation.NonNull
import com.example.wbudy_apka.location.Position
import com.example.wbudy_apka.model.LatLong
import com.example.wbudy_apka.model.TimeOfDay
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity: FlutterActivity() {
    private val SENSORS_CHANNEL = "samples.flutter.dev/sensors"
    private val GPS_CHANNEL = "samples.flutter.dev/gps"
    private val OTHER_CHANNEL = "samples.flutter.dev/other"
    private val CONFIGURATION_CHANNEL = "samples.flutter.dev/configuration"
    
    val gyroListener = PositionAndMotionListener()
    val accListener = PositionAndMotionListener()
    val magneticListener = PositionAndMotionListener()
    val lightListener = EnvirementalListener()

    private lateinit var configuration: Configuration

    private lateinit var wbudyServiceIntent: Intent
    private lateinit var wbudyService: WbudyService
    private var wbudyServiceConnected: Boolean = false
    private val wbudyServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder: WbudyService.WbudyServiceBinder = service as WbudyService.WbudyServiceBinder
            wbudyService = binder.service
            wbudyServiceConnected = true
        }
        override fun onServiceDisconnected(arg0: ComponentName) {
            wbudyServiceConnected = false
        }
    }

    override fun onStart() {
        super.onStart()
        configuration = Configuration(this.applicationContext)
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
                "startGps" -> startService()
                "getPosition" -> getPosition(result)
                "isPositionAvailable" -> isPositionAvailable(result)
                "isNMEAWorks" -> isNMEAWorks(result)
                else -> {
                    result.notImplemented();
                }
            }
        }
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger,OTHER_CHANNEL).setMethodCallHandler {
            call, result ->
            when(call.method) {
                "getChildState" -> getChildState(result)
                else -> {
                    result.notImplemented()
                }
            }
        }
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger,CONFIGURATION_CHANNEL).setMethodCallHandler {
            call, result ->
            when(call.method) {
                "getSchoolPosition" -> {
                    val x = HashMap<String,Double>()
                    val schoolPosition = configuration.getSchoolPosition()
                    x.put("lat",schoolPosition.latitude)
                    x.put("long",schoolPosition.longtitude)
                    result.success(x)
                }
                "setSchoolPosition" -> {
                    val lat = call.argument<Double>("lat")
                    val long = call.argument<Double>("long")
                    if(lat != null && long != null){
                        configuration.setSchoolPosition(LatLong(lat,long))
                        result.success(true)
                    } else {
                        result.success(false)
                    }
                }
                "isAppConfigured" -> {
                    result.success(configuration.isAppConfigured())
                }
                "setAppConfigured" -> {
                    val configured = call.argument<Boolean>("configured")
                    if(configured != null) {
                        configuration.setAppConfigured(configured)
                        result.success(true)
                    } else {
                        result.success(false)
                    }
                }
                "getDeviceOwner" -> {
                    result.success(configuration.getDeviceOwner())
                }
                "setDeviceOwner" -> {
                    val deviceOwner = call.argument<String>("deviceOwner")
                    if(deviceOwner != null) {
                        configuration.setDeviceOwner(deviceOwner)
                        result.success(true)
                    } else {
                        result.success(false)
                    }
                }
                "getSchoolStartAt" -> {
                    val x = HashMap<String,Int>()
                    val time = configuration.getSchoolStartAt()
                    x.put("h",time.hours)
                    x.put("m",time.minutes)
                    result.success(x)
                }
                "setSchoolStartAt" -> {
                    val h = call.argument<Int>("h")
                    val m = call.argument<Int>("m")
                    if(h != null && m != null) {
                        configuration.setSchoolStartAt(TimeOfDay(h,m))
                        result.success(true)
                    } else {
                        result.success(false)
                    }
                }
                "getSchoolEndAt" -> {
                    val x = HashMap<String,Int>()
                    val time = configuration.getSchoolEndAt()
                    x.put("h",time.hours)
                    x.put("m",time.minutes)
                    result.success(x)
                }
                "setSchoolEndAt" -> {
                    val h = call.argument<Int>("h")
                    val m = call.argument<Int>("m")
                    if(h != null && m != null) {
                        configuration.setSchoolEndAt(TimeOfDay(h,m))
                        result.success(true)
                    } else {
                        result.success(false)
                    }
                }
                else -> {
                    result.notImplemented()
                }
            }
        }
    }
    private fun getChildState(result:MethodChannel.Result) {
        result.success(wbudyService.getChildStateHashMap())
    }
    private fun startService() {
        wbudyServiceIntent = Intent(this,WbudyService::class.java)
        startService(wbudyServiceIntent)
        bindService(wbudyServiceIntent,wbudyServiceConnection,Context.BIND_AUTO_CREATE)
    }

    private fun isPositionAvailable(result:MethodChannel.Result) {
        result.success(wbudyServiceConnected)
    }

    private fun isNMEAWorks(result:MethodChannel.Result) {
        result.success(wbudyService.isNMEAWorks())
    }

    private fun getPosition(result:MethodChannel.Result) {
        var positionHashMap: HashMap<String,String> = HashMap<String,String>()
        if (!wbudyServiceConnected){
            positionHashMap.put("isPositionAvailable",wbudyServiceConnected.toString())
        } else {
          val position = wbudyService.lastPosition
          positionHashMap.put("longtitude", position.longitude.longitude.toString())
          positionHashMap.put("latitude", position.latitude.latitude.toString())
          positionHashMap.put("time",position.datetime.toString())
          positionHashMap.put("isPositionAvailable",wbudyServiceConnected.toString())
          if(position.availableAltitude)
              positionHashMap.put("altitude",position.altitude.toString())
        }
        result.success(positionHashMap)
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