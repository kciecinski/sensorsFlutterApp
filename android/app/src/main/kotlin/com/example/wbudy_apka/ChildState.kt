package com.example.wbudy_apka

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.wbudy_apka.location.Latitude
import com.example.wbudy_apka.location.Longitude
import com.example.wbudy_apka.location.Position
import com.example.wbudy_apka.model.LatLong

class ChildState(private var context: Context) {
    private var _currentPosition: LatLong = LatLong(0.0,0.0)
    private var _configuration: Configuration = Configuration(context)
    private var hashMap: HashMap<String,String> =  HashMap<String,String>()
    var currentPosition: LatLong
    get() {
        return _currentPosition
    }
    set(value) {
        _currentPosition = value
        update()
    }
    private fun update() {
        hashMap.put("distanceToSchool",getDistanceToSchool().toString())
        hashMap.put("shouldBeInSchool",isShouldBeInSchool().toString())
        hashMap.put("inSchool",isInSchool().toString())
    }
    fun getRadiusCricleForCheckIsInSchool(): Double {
        return 100.0;
    }
    fun getDistanceToSchool(): Double {
        return currentPosition.distanceInKilometers(_configuration.getSchoolPosition())
    }
    fun isShouldBeInSchool(): Boolean {
        return true;
    }
    fun isInSchool(): Boolean {
        return getDistanceToSchool() <= getRadiusCricleForCheckIsInSchool()
    }
    fun asHashMap(): HashMap<String,String> {
        return hashMap
    }
}