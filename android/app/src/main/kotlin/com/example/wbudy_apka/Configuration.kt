package com.example.wbudy_apka


import android.content.Context
import android.content.SharedPreferences
import com.example.wbudy_apka.model.LatLong
import com.example.wbudy_apka.model.TimeOfDay

class Configuration(private var context: Context) {
    companion object {
        val OwnerParent = "Parent"
        val OwnerChild = "Child"
    }
    private val PREFIX = "config."
    private val SHARED_PREFERENCES_NAME: String = "WbudyAppSharedPreferences"
    private val LIST_IDENTIFIER: String = "VGhpcyBpcyB0aGUgcHJlZml4IGZvciBhIGxpc3Qu";
    private val BIG_INTEGER_PREFIX: String = "VGhpcyBpcyB0aGUgcHJlZml4IGZvciBCaWdJbnRlZ2Vy";
    private val DOUBLE_PREFIX: String = "VGhpcyBpcyB0aGUgcHJlZml4IGZvciBEb3VibGUu";
    private val prefs :SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
    private fun containsKey(key: String): Boolean {
        return prefs.contains(PREFIX+key)
    }
    fun getDouble(key: String): Double {
        if(!containsKey(key)) {
            throw error("Value ${key} not exist")
        }
        var x = prefs.getString(PREFIX+key,DOUBLE_PREFIX+"0.0")
        if(x != null) {
            x = x.substring(DOUBLE_PREFIX.length)
            return x.toDouble()
        }
        throw error("Value ${key} not exist")
    }
    fun setDouble(key: String,value: Double) {
        val editor = prefs.edit()
        if(value == 0.0) {
            editor.putString(PREFIX+key,DOUBLE_PREFIX+"0.0")
        } else {
            editor.putString(PREFIX+key,DOUBLE_PREFIX+value.toString())
        }
        editor.commit()
    }
    fun getInt(key: String): Int {
        if(!containsKey(key)) {
            throw error("Value ${key} not exist")
        }
        return prefs.getInt(PREFIX+key,0)
    }
    fun setInt(key: String, value: Int) {
        val editor = prefs.edit()
        editor.putInt(PREFIX+key,value)
        editor.commit()
    }
    fun getBool(key: String): Boolean {
        if(!containsKey(key)) {
            throw error("Value ${key} not exist")
        }
        return prefs.getBoolean(PREFIX+key,false)
    }
    fun setBool(key: String, value: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(PREFIX+key,value)
        editor.commit()
    }
    fun getString(key: String): String {
        if(!containsKey(key)) {
            throw error("Value ${key} not exist")
        }
        val x = prefs.getString(PREFIX+key,"")
        if(x != null) {
            return x
        }
        throw error("Value ${key} not exist")
    }
    fun setString(key: String, value: String) {
        val editor = prefs.edit()
        editor.putString(PREFIX+key,value)
        editor.commit()
    }

    fun getSchoolPosition(): LatLong {
        val keyLat = "latSchool"
        val keyLong = "longSchool"
        val lat = getDouble(keyLat)
        val long = getDouble(keyLong)
        return LatLong(lat,long)
    }
    fun setSchoolPosition(value: LatLong) {
        val keyLat = "latSchool"
        val keyLong = "longSchool"
        setDouble(keyLat,value.latitude)
        setDouble(keyLong,value.longtitude)
    }
    fun isAppConfigured(): Boolean {
        val key = "appConfigured"
        if(containsKey(key))
            return getBool(key)
        else
            return false
    }
    fun setAppConfigured(value: Boolean) {
        val key = "appConfigured"
        setBool(key,value)
    }
    fun getDeviceOwner(): String {
        val key = "deviceOwner";
        return getString(key)
    }
    fun setDeviceOwner(value: String) {
        val key = "deviceOwner";
        setString(key,value)
    }
    fun getSchoolStartAt(): TimeOfDay {
        val keyH = "schoolStartAt_hour"
        val keyM = "schoolStartAt_minute"
        val h = getInt(keyH)
        val m = getInt(keyM)
        return TimeOfDay(h,m)
    }
    fun setSchoolStartAt(value: TimeOfDay) {
        val keyH = "schoolStartAt_hour"
        val keyM = "schoolStartAt_minute"
        setInt(keyH,value.hours)
        setInt(keyM,value.minutes)
    }
    fun getSchoolEndAt(): TimeOfDay {
        val keyH = "schoolEndAt_hour"
        val keyM = "schoolEndAt_minute"
        val h = getInt(keyH)
        val m = getInt(keyM)
        return TimeOfDay(h,m)
    }
    fun setSchoolEndAt(value: TimeOfDay) {
        val keyH = "schoolEndAt_hour"
        val keyM = "schoolEndAt_minute"
        setInt(keyH,value.hours)
        setInt(keyM,value.minutes)
    }
}