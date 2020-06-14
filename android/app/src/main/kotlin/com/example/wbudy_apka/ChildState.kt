package com.example.wbudy_apka

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.example.wbudy_apka.location.Position
import com.example.wbudy_apka.location.PositionListener
import com.example.wbudy_apka.math.Quaternion
import com.example.wbudy_apka.math.Vector3
import com.example.wbudy_apka.model.LatLong
import com.example.wbudy_apka.model.TimeOfDay

class ChildState(private var context: Context) : PositionListener, SensorEventListener {
    private var sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var configuration: Configuration = Configuration(context)
    private var lastPosition: LatLong = LatLong(0.0,0.0)
    private var lastMagneticField: Vector3 = Vector3(0.0,0.0,0.0);
    fun getRadiusCricleForCheckIsInSchool(): Double {
        return 100.0/1000;
    }
    fun getDistanceToSchool(): Double {
        return lastPosition.distanceInKilometers(configuration.getSchoolPosition())
    }
    fun isShouldBeInSchool(): Boolean {
        val now = TimeOfDay.now()
        return now.isInRange(configuration.getSchoolStartAt(),configuration.getSchoolEndAt())
    }
    fun isInSchool(): Boolean {
        return getDistanceToSchool() <= getRadiusCricleForCheckIsInSchool()
    }
    fun isWithoutEtui(): Boolean {
        return configuration.getRangeMagneticFieldLengthWithoutEtui().isInRangeInclusive(lastMagneticField.length);
    }

    /**
     * Handler for incomming position
     */
    override fun newPosition(position: Position) {
        this.lastPosition = position.latlong;
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        if(event == null)
            return
        when(event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                //Log.i("TYPE_ACCELEROMETER", event.values.size.toString())
            }
            Sensor.TYPE_GYROSCOPE -> {
                //Log.i("TYPE_GYROSCOPE", event.values.size.toString())
            }
            Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED -> {
                lastMagneticField = Vector3(event.values[0].toDouble(),event.values[1].toDouble(),event.values[2].toDouble())
            }
            Sensor.TYPE_LIGHT -> {
                //Log.i("TYPE_LIGHT", event.values.size.toString())
                //Log.i("TYPE_LIGHT",event.values.contentToString())
            }
            else -> {
                Log.e("ChildState","Unimplemented for this sensor");
            }
        }
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