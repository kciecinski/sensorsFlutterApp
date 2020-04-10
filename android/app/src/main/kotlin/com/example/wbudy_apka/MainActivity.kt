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
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {
    private val CHANNEL = "samples.flutter.dev/gyroscope"
    
    val listener = GyroscopeListener();

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler {
            call, result ->
            if (call.method == "getGyroscopeValues") {
                val gyroValues = getGyroscopeValues()
        
                if (gyroValues != {}) {
                  result.success(gyroValues)
                } else {
                  result.error("UNAVAILABLE", "Gryoscope not available.", null)
                }
              } else {
                result.notImplemented()
              }    
         }
    }

    private fun getGyroscopeValues(): HashMap<String,Double> {
        val sensorManager =  getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        return listener.gyroValues;
      }
}

class GyroscopeListener : SensorEventListener {

    val gyroValues:HashMap<String,Double> = HashMap<String,Double>() 
  
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
  
    }
    override fun onSensorChanged(event: SensorEvent?) {
        val sensorName: String = event?.sensor!!.getName();
        gyroValues.put("x", event.values[0].toDouble());
        gyroValues.put("y", event.values[1].toDouble());
        gyroValues.put("z", event.values[2].toDouble());
    }
    
}