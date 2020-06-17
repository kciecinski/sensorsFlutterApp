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
                if(_isCalibratingMotionDetect_acceleration) {
                    val acceleration_vec = Vector3(event.values[0].toDouble(),event.values[1].toDouble(),event.values[2].toDouble())
                    val acceleration = acceleration_vec.length
                    val minAcceleration = mapD.get("minAcceleration")
                    val maxAcceleration = mapD.get("maxAcceleration")
                    if(minAcceleration == null || maxAcceleration == null) {
                        mapD.set("minAcceleration",acceleration)
                        mapD.set("maxAcceleration",acceleration)
                        mapI.set("probeWhenNotChangeAcceleration",0);
                    }
                    else {
                        var probeWhenNotChange = mapI.get("probeWhenNotChangeAcceleration")
                        if(probeWhenNotChange == null) probeWhenNotChange = 0
                        if(acceleration < minAcceleration) {
                            mapD.set("minAcceleration",acceleration)
                            mapI.set("probeWhenNotChangeAcceleration",0);
                        }
                        else if(maxAcceleration < acceleration) {
                            mapD.set("maxAcceleration",acceleration)
                            mapI.set("probeWhenNotChangeAcceleration",0);
                        }
                        else {
                            probeWhenNotChange += 1
                            mapI.set("probeWhenNotChangeAcceleration",probeWhenNotChange)
                        }
                        if(probeWhenNotChange > 50) {
                            val min = mapD.get("minAcceleration");
                            val max = mapD.get("maxAcceleration");
                            if(min != null && max != null) {
                                _isCalibratingMotionDetect_acceleration = false
                                configuration.setRangeAccelerationWithoutMotion(RangeDouble(min,max))
                                if(!isCalibratingMotionDetect) {
                                    configuration.setConfiguredMotionDetector(true)
                                }
                            }
                        }
                    }
                }
            }
        Sensor.TYPE_GYROSCOPE -> {
            if(_isCalibratingMotionDetect_gyro) {
                val gyro_vec = Vector3(event.values[0].toDouble(),event.values[1].toDouble(),event.values[2].toDouble())
                val gyro = gyro_vec.length
                val minGyro = mapD.get("minGyro")
                val maxGyro = mapD.get("maxGyro")
                if(minGyro == null || maxGyro == null) {
                    mapD.set("minGyro",gyro)
                    mapD.set("maxGyro",gyro)
                    mapI.set("probeWhenNotChangeGyro",0);
                }
                else {
                    var probeWhenNotChange = mapI.get("probeWhenNotChangeGyro")
                    if(probeWhenNotChange == null) probeWhenNotChange = 0
                    if(gyro < minGyro) {
                        mapD.set("minGyro",gyro)
                        mapI.set("probeWhenNotChangeGyro",0);
                    }
                    else if(maxGyro < gyro) {
                        mapD.set("maxGyro",gyro)
                        mapI.set("probeWhenNotChangeGyro",0);
                    }
                    else {
                        probeWhenNotChange += 1
                        mapI.set("probeWhenNotChangeGyro",probeWhenNotChange)
                    }
                    if(probeWhenNotChange > 50) {
                        val min = mapD.get("minGyro");
                        val max = mapD.get("maxGyro");
                        if(min != null && max != null) {
                            _isCalibratingMotionDetect_gyro = false
                            mapD.clear()
                            mapI.clear()
                            configuration.setRangeRotationWithoutMotion(RangeDouble(min,max))
                            if(!isCalibratingMotionDetect) {
                                configuration.setConfiguredMotionDetector(true)
                            }
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
        _isCalibratingMotionDetect_acceleration = true
        _isCalibratingMotionDetect_gyro = true
        start()
    }
    private var _isCalibratingMotionDetect_acceleration: Boolean = false;
    private var _isCalibratingMotionDetect_gyro: Boolean = false;
    val isCalibratingMotionDetect: Boolean
    get() {
        return _isCalibratingMotionDetect_acceleration || _isCalibratingMotionDetect_gyro;
    }

    private val isSomethingIsCalibating: Boolean
        get() { return isCalibratingMotionDetect || isCalibratingWithoutEtui }

    fun start() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED), SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL)
    }
    fun stop() {
        mapD.clear()
        mapI.clear()
        sensorManager.unregisterListener(this)
    }


}