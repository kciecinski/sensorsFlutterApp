package com.example.wbudy_apka.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent

class EnvirementalListener : BasicSensoreListener {

    override val values:HashMap<String,Double> = HashMap<String,Double>()

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    override fun onSensorChanged(event: SensorEvent?) {
        val sensorName: String = event?.sensor!!.getName();
        values.put("value", event.values[0].toDouble());
    }
}