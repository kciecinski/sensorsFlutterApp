package com.example.wbudy_apka

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.example.wbudy_apka.model.RangeDouble


class Calibrate(private var context: Context): SensorEventListener {
    private var sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var configuration: Configuration = Configuration(context);
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    var mapD = HashMap<String,Double>()
    var mapI = HashMap<String,Int>()

    override fun onSensorChanged(event: SensorEvent?) {
        if(event == null)
            return

        when(event.sensor.type) {
            Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED -> {
                if(_isCalibratingWithoutEtui) {
                    var x = event.values[0].toDouble();
                    var y = event.values[1].toDouble();
                    var z= event.values[2].toDouble();
                    var l = Math.sqrt((x*x)+(y*y)+(z*z));
                    val min = mapD.get("min");
                    val max = mapD.get("max");
                    var probeWhenNotChange = mapI.get("probeWhenNotChange");
                    if(probeWhenNotChange == null) {
                        probeWhenNotChange = 0;
                    }
                    if(min != null && max != null) {
                        if(l < min) {
                            mapD.set("min",l)
                            mapI.set("probeWhenNotChange",0);
                        } else if (l > max) {
                            mapD.set("max",l)
                            mapI.set("probeWhenNotChange",0);
                        }
                        else {
                            probeWhenNotChange += 1;
                            mapI.set("probeWhenNotChange",probeWhenNotChange);
                        }
                    } else {
                        mapD.set("min",l)
                        mapD.set("max",l)
                        mapI.set("probeWhenNotChange",0);
                    }
                    Log.i("Calibrate","${probeWhenNotChange} ${max} ${min}")
                    if(probeWhenNotChange > 50) {
                        val min = mapD.get("min");
                        val max = mapD.get("max");
                        if(min != null && max != null) {
                            stop();
                            _isCalibratingWithoutEtui = false
                            _isCalibratedWithoutEtui = true
                            mapD.clear()
                            mapI.clear()
                            configuration.setRangeMagneticFieldLengthWithoutEtui(RangeDouble(min,max));
                        }
                    }
                }
            }
        }
    }

    fun startCalibrateWithoutEtui() {
        _isCalibratingWithoutEtui = true;
        _isCalibratedWithoutEtui = false;
        start()
    }
    private var _isCalibratingWithoutEtui: Boolean = false;
    private var _isCalibratedWithoutEtui: Boolean = false;
    val isCalibratedWithoutEtui: Boolean
        get() {
            return _isCalibratedWithoutEtui && !_isCalibratingWithoutEtui
        }
    val isCalibratingWithoutEtui: Boolean
        get() {
            return _isCalibratingWithoutEtui
        }

    fun start() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED), SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL)
    }
    fun stop() {
        sensorManager.unregisterListener(this)
    }
}