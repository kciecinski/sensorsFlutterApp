package com.example.wbudy_apka

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.example.wbudy_apka.math.Vector3
import com.example.wbudy_apka.model.RangeDouble


class Calibrate(private var context: Context): SensorEventListener {
    private var sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var configuration: Configuration = Configuration(context);
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    var mapD = HashMap<String,Double>()
    var mapI = HashMap<String,Int>()

    override fun onSensorChanged(event: SensorEvent?) {
        if(!isSomethingIsCalibating){
            stop()
            return
        }

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
                    Log.i("Calibrate without etui","${probeWhenNotChange} ${max} ${min}")
                    if(probeWhenNotChange > 50) {
                        val min = mapD.get("min");
                        val max = mapD.get("max");
                        if(min != null && max != null) {
                            _isCalibratingWithoutEtui = false
                            mapD.clear()
                            mapI.clear()
                            configuration.setRangeMagneticFieldLengthWithoutEtui(RangeDouble(min,max));
                        }
                    }
                }
            }
            Sensor.TYPE_ACCELEROMETER -> {
                if(_isCalibratingMotionDetect) {
                    val acceleration = Vector3(event.values[0].toDouble(),event.values[1].toDouble(),event.values[2].toDouble())
                    val accelerationL = acceleration.length
                    val min = mapD.get("min")
                    val max = mapD.get("max")
                    if(min == null || max == null) {
                        mapD.set("min",accelerationL)
                        mapD.set("max",accelerationL)
                        mapI.set("probeWhenNotChange",0);
                    }
                    else {
                        var probeWhenNotChange = mapI.get("probeWhenNotChange")
                        if(probeWhenNotChange == null) probeWhenNotChange = 0
                        if(accelerationL < min) {
                            mapD.set("min",accelerationL)
                            mapI.set("probeWhenNotChange",0);
                        }
                        else if(max < accelerationL) {
                            mapD.set("max",accelerationL)
                            mapI.set("probeWhenNotChange",0);
                        }
                        else {
                            probeWhenNotChange += 1
                            mapI.set("probeWhenNotChange",probeWhenNotChange)
                        }
                        if(probeWhenNotChange > 50) {
                            val min = mapD.get("min");
                            val max = mapD.get("max");
                            if(min != null && max != null) {
                                _isCalibratingMotionDetect = false
                                mapD.clear()
                                mapI.clear()
                                configuration.setRangeAccelerationWithoutMotion(RangeDouble(min,max))
                                configuration.setConfiguredMotionDetector(true)
                            }
                        }
                    }
                }
            }
        }
    }

    fun startCalibrateWithoutEtui() {
        _isCalibratingWithoutEtui = true
        start()
    }
    private var _isCalibratingWithoutEtui: Boolean = false;
    val isCalibratingWithoutEtui: Boolean
        get() {
            return _isCalibratingWithoutEtui
        }
    fun startCalibrateMotionDetect() {
        _isCalibratingMotionDetect = true
        start()
    }
    private var _isCalibratingMotionDetect: Boolean = false;
    val isCalibratingMotionDetect: Boolean
    get() {
        return _isCalibratingMotionDetect;
    }

    private val isSomethingIsCalibating: Boolean
        get() { return isCalibratingMotionDetect || isCalibratingWithoutEtui }

    fun start() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED), SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)

    }
    fun stop() {
        mapD.clear()
        mapI.clear()
        sensorManager.unregisterListener(this)
    }


}