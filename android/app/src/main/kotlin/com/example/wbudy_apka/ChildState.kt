package com.example.wbudy_apka

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.wbudy_apka.location.Latitude
import com.example.wbudy_apka.location.Longitude
import com.example.wbudy_apka.location.Position
import com.example.wbudy_apka.model.LatLong
import com.example.wbudy_apka.model.TimeOfDay

class ChildState(private var context: Context) {
    private var _currentPosition: LatLong = LatLong(0.0,0.0)
    private var _configuration: Configuration = Configuration(context)
    var currentPosition: LatLong
    get() {
        return _currentPosition
    }
    set(value) {
        _currentPosition = value
    }
    fun getRadiusCricleForCheckIsInSchool(): Double {
        return 100.0;
    }
    fun getDistanceToSchool(): Double {
        return currentPosition.distanceInKilometers(_configuration.getSchoolPosition())
    }
    fun isShouldBeInSchool(): Boolean {
        val now = TimeOfDay.now()
        Log.i("isShouldBeInSchool","now: "+now)
        Log.i("isShouldBeInSchool","start: "+_configuration.getSchoolStartAt())
        Log.i("isShouldBeInSchool","end: "+_configuration.getSchoolEndAt())

        return now.isInRange(_configuration.getSchoolStartAt(),_configuration.getSchoolEndAt())
    }
    fun isInSchool(): Boolean {
        return getDistanceToSchool() <= getRadiusCricleForCheckIsInSchool()
    }
}