package com.example.wbudy_apka

import androidx.annotation.NonNull;
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugins.GeneratedPluginRegistrant
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    private val CHANNEL = "samples.flutter.dev/sensors"
    
    val gyroListener = PositionAndMotionListener();
    val accListener = PositionAndMotionListener();
    val magneticListener = PositionAndMotionListener();
    val lightListener = EnvirementalListener();

    val permissionRequestCode = 123
    var neededPermissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
    private lateinit var locationNMEAServiceIntent: Intent

    override fun onStart() {
        getPermissionsAndStart()
        super.onStart()
    }

    fun getPermissionsAndStart() {
        var askForPermissions: MutableList<String> = ArrayList<String>()
        for(permission in neededPermissions) {
            if (ContextCompat.checkSelfPermission(this@MainActivity,permission) == PackageManager.PERMISSION_DENIED) {
                askForPermissions.add(permission)
            }
        }
        if(!askForPermissions.isEmpty())
            ActivityCompat.requestPermissions(this@MainActivity, askForPermissions.toTypedArray(), permissionRequestCode)
        else
            startWithPermissions()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var haveAllPermission = true
        if (requestCode == permissionRequestCode){
            for((index,permission) in permissions.withIndex()) {
                if(neededPermissions.contains(permission) && grantResults[index] == PackageManager.PERMISSION_DENIED) {
                    haveAllPermission = false
                    break
                }
            }
        }
        if(haveAllPermission)
            startWithPermissions()
    }

    private fun startWithPermissions() {
        locationNMEAServiceIntent = Intent(this,LocationNMEAService::class.java)
        startService(locationNMEAServiceIntent)
    }

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
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