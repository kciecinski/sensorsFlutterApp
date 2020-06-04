package com.example.wbudy_apka


import android.content.Context
import android.content.SharedPreferences
import com.example.wbudy_apka.model.LatLong

class Configuration(private var context: Context) {
    private val flutterPrefix = "flutter."
    private val LIST_IDENTIFIER: String = "VGhpcyBpcyB0aGUgcHJlZml4IGZvciBhIGxpc3Qu";
    private val BIG_INTEGER_PREFIX: String = "VGhpcyBpcyB0aGUgcHJlZml4IGZvciBCaWdJbnRlZ2Vy";
    private val DOUBLE_PREFIX: String = "VGhpcyBpcyB0aGUgcHJlZml4IGZvciBEb3VibGUu";
    private val prefs :SharedPreferences = context.getSharedPreferences("FlutterSharedPreferences",Context.MODE_PRIVATE);
    private fun containsKey(key: String): Boolean {
        return prefs.contains(flutterPrefix+key)
    }
    private fun getDouble(key: String): Double {
        if(!containsKey(key)) {
            throw error("Value ${key} not exist")
        }
        var x = prefs.getString(flutterPrefix+key,DOUBLE_PREFIX+"0.0")
        if(x != null) {
            x = x.substring(DOUBLE_PREFIX.length)
            return x.toDouble()
        }
        throw error("Value ${key} not exist")
    }
    fun getSchoolPosition(): LatLong {
        val keyLat = "latSchool"
        val keyLong = "longSchool"
        val lat = getDouble(keyLat)
        val long = getDouble(keyLong)
        return LatLong(lat,long)
    }
}