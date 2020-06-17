package com.example.wbudy_apka


import android.content.Context
import android.content.SharedPreferences
import com.example.wbudy_apka.model.LatLong
import com.example.wbudy_apka.model.RangeDouble
import com.example.wbudy_apka.model.TimeOfDay

class Configuration(private var context: Context) {
    companion object {
        val OwnerParent = "Parent"
        val OwnerChild = "Child"
    }
    private val SHARED_PREFERENCES_NAME: String = "WbudyAppSharedPreferences"
    private val LIST_IDENTIFIER: String = "VGhpcyBpcyB0aGUgcHJlZml4IGZvciBhIGxpc3Qu";
    private val BIG_INTEGER_PREFIX: String = "VGhpcyBpcyB0aGUgcHJlZml4IGZvciBCaWdJbnRlZ2Vy";
    private val DOUBLE_PREFIX: String = "VGhpcyBpcyB0aGUgcHJlZml4IGZvciBEb3VibGUu";
    private val prefs :SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE);
    private fun containsKey(key: String): Boolean {
        return prefs.contains(key)
    }
    fun getDouble(key: String): Double {
        if(!containsKey(key)) {
            throw error("Value ${key} not exist")
        }
        var x = prefs.getString(key,DOUBLE_PREFIX+"0.0")
        if(x != null) {
            x = x.substring(DOUBLE_PREFIX.length)
            return x.toDouble()
        }
        throw error("Value ${key} not exist")
    }
    fun setDouble(key: String,value: Double) {
        val editor = prefs.edit()
        if(value == 0.0) {
            editor.putString(key,DOUBLE_PREFIX+"0.0")
        } else {
            editor.putString(key,DOUBLE_PREFIX+value.toString())
        }
        editor.commit()
    }
    fun getInt(key: String): Int {
        if(!containsKey(key)) {
            throw error("Value ${key} not exist")
        }
        return prefs.getInt(key,0)
    }
    fun setInt(key: String, value: Int) {
        val editor = prefs.edit()
        editor.putInt(key,value)
        editor.commit()
    }
    fun getBool(key: String): Boolean {
        if(!containsKey(key)) {
            throw error("Value ${key} not exist")
        }
        return prefs.getBoolean(key,false)
    }
    fun setBool(key: String, value: Boolean) {
        val editor = prefs.edit()
        editor.putBoolean(key,value)
        editor.commit()
    }
    fun getString(key: String): String {
        if(!containsKey(key)) {
            throw error("Value ${key} not exist")
        }
        val x = prefs.getString(key,"")
        if(x != null) {
            return x
        }
        throw error("Value ${key} not exist")
    }
    fun setString(key: String, value: String) {
        val editor = prefs.edit()
        editor.putString(key,value)
        editor.commit()
    }
    fun getLatLong(key: String): LatLong {
        val suffix = ".latlong"
        val suffixlat = ".lat"
        val suffixlong = ".long"
        val fullKeyLat = key+suffix+suffixlat
        val fullKeyLong: String = key+suffix+suffixlong
        val lat = getDouble(fullKeyLat)
        val long: Double = getDouble(fullKeyLong)
        return LatLong(lat,long)
    }
    fun setLatLong(key: String, value: LatLong) {
        val suffix = ".latlong"
        val suffixlat = ".lat"
        val suffixlong = ".long"
        val fullKeyLat = key+suffix+suffixlat
        val fullKeyLong: String = key+suffix+suffixlong
        setDouble(fullKeyLat,value.latitude)
        setDouble(fullKeyLong,value.longtitude)
    }
    fun setRangeDouble(key: String, value: RangeDouble) {
        val suffixMin = ".range.min"
        val suffixMax = ".range.max"
        val fullKeyMin = key+suffixMin
        val fullKeyMax = key+suffixMax
        setDouble(fullKeyMin,value.min)
        setDouble(fullKeyMax,value.max)
    }
    fun getRangeDouble(key: String): RangeDouble {
        val suffixMin = ".range.min"
        val suffixMax = ".range.max"
        val fullKeyMin = key+suffixMin
        val fullKeyMax = key+suffixMax
        val min = getDouble(fullKeyMin)
        val max =getDouble(fullKeyMax)
        return RangeDouble(min,max);
    }

    fun getSchoolPosition(): LatLong {
        val key = "schoolPosition"
        return getLatLong(key)

    }
    fun setSchoolPosition(value: LatLong) {
        val key = "schoolPosition"
        setLatLong(key,value)
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
    fun isHaveEtui(): Boolean {
        val key = "haveEtui"
        if(containsKey(key))
            return getBool(key)
        else
            return false
    }
    fun setHaveEtui(value: Boolean) {
        val key = "haveEtui"
        setBool(key,value)
    }
    fun isConfiguredSchool(): Boolean {
        val key = "configuredSchool"
        if(containsKey(key))
            return getBool(key)
        else
            return false
    }
    fun setConfiguredSchool(value: Boolean) {
        val key = "configuredSchool"
        setBool(key,value)
    }
    fun isConfiguredEtui(): Boolean {
        val key = "configuredEtui"
        if(containsKey(key))
            return getBool(key)
        else
            return false
    }
    fun setConfiguredEtui(value: Boolean) {
        val key = "configuredEtui"
        setBool(key,value)
    }
    fun getRangeMagneticFieldLengthWithoutEtui(): RangeDouble {
        val key = "rangeMagneticFieldLengthWithoutEtui"
        return getRangeDouble(key);
    }
    fun setRangeMagneticFieldLengthWithoutEtui(value: RangeDouble) {
        val key = "rangeMagneticFieldLengthWithoutEtui"
        setRangeDouble(key,value)
    }

    fun getRangeAccelerationWithoutMotion(): RangeDouble {
        val key = "rangeAccelerationWithoutMotion"
        return getRangeDouble(key);
    }

    fun setRangeAccelerationWithoutMotion(value: RangeDouble) {
        val key = "rangeAccelerationWithoutMotion"
        setRangeDouble(key,value)
    }
    fun isConfiguredMotionDetector(): Boolean {
        val key = "configuredMotionDetector"
        if(containsKey(key))
            return getBool(key)
        else
            return false
    }
    fun setConfiguredMotionDetector(value: Boolean) {
        val key = "configuredMotionDetector"
        setBool(key,value)
    }
}