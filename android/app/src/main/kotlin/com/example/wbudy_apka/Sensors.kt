package com.example.wbudy_apka

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import com.example.wbudy_apka.sensors.EnvirementalListener
import com.example.wbudy_apka.sensors.PositionAndMotionListener

class Sensors(private var context: Context) {
    private var sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val _gyroListener = PositionAndMotionListener()
    private val _accListener = PositionAndMotionListener()
    private val _magneticListener = PositionAndMotionListener()
    private val _lightListener = EnvirementalListener()
    private val _proxmityListener = EnvirementalListener()
    val gyroListener: PositionAndMotionListener get() { return _gyroListener }
    val accListener: PositionAndMotionListener get() { return _accListener }
    val magneticListener: PositionAndMotionListener get() { return _magneticListener }
    val lightListener: EnvirementalListener get() { return _lightListener }
    val proxmityListener: EnvirementalListener get() { return _proxmityListener }

    fun start() {
        sensorManager.registerListener(accListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(gyroListener, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(magneticListener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED), SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(lightListener, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(proxmityListener, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL)
    }
    fun stop() {
        sensorManager.unregisterListener(accListener)
        sensorManager.unregisterListener(gyroListener)
        sensorManager.unregisterListener(magneticListener)
        sensorManager.unregisterListener(lightListener)
        sensorManager.unregisterListener(proxmityListener)
    }
}