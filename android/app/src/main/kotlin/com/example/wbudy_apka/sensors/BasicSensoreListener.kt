package com.example.wbudy_apka.sensors

import android.hardware.SensorEventListener

interface BasicSensoreListener: SensorEventListener {
    val values:HashMap<String,Double>;
}