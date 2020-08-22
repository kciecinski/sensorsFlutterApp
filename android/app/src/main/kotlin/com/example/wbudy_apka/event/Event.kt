package com.example.wbudy_apka.event

import com.example.wbudy_apka.ChildState
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap

class Event(var timestamp: Long, var isWithoutEtui: ChildState.WithoutEtuiStates,
            var distanceToSchool: Double, var isInSchool: Boolean,
            var isShouldBeInSchool: Boolean, var isPhoneHidden: Boolean,
            var isInMotion: Boolean, var id: Long = -1) {
    override fun toString(): String {
        val x = "\"timestamp\":$timestamp, \"isWithoutEtui\":\"$isWithoutEtui\", \"distanceToSchool\":$distanceToSchool, " +
                "\"isInSchool\":\"$isInSchool\", \"isShouldBeInSchool\":\"$isShouldBeInSchool\", \"isPhoneHidden\":\"$isPhoneHidden\", \"isInMotion\":\"$isInMotion\", \"id\": \"$id\""
        return "{$x}"
    }
    fun toDictionary(): HashMap<String,String> {
        var hashMap = HashMap<String,String>()
        hashMap.put("timestamp",timestamp.toString())
        hashMap.put("isWithoutEtui",isWithoutEtui.toString())
        hashMap.put("distanceToSchool",distanceToSchool.toString())
        hashMap.put("isInSchool",isInSchool.toString())
        hashMap.put("isShouldBeInSchool",isShouldBeInSchool.toString())
        hashMap.put("isPhoneHidden",isPhoneHidden.toString())
        hashMap.put("isInMotion",isInMotion.toString())
        hashMap.put("id",id.toString())
        return hashMap
    }
    companion object {
        private fun strToBoolean(str: String): Boolean {
            if(str == "true") return true
            else return false
        }
        fun fromJsonString(jsonString: String): Event {
            val jsonObject = JSONObject(jsonString);
            return Event(
                    jsonObject.getLong("timestamp"),
                    ChildState.WithoutEtuiStates.FromString(jsonObject.getString("isWithoutEtui")),
                    jsonObject.getDouble("distanceToSchool"),
                    strToBoolean(jsonObject.getString("isInSchool")),
                    strToBoolean(jsonObject.getString("isShouldBeInSchool")),
                    strToBoolean(jsonObject.getString("isPhoneHidden")),
                    strToBoolean(jsonObject.getString("isInMotion")),
                    jsonObject.getLong("id")
            );
        }
        val invalidEvent: Event get() { return Event(0,ChildState.WithoutEtuiStates.DO_NOT_HAVE_ANY,0.0,false,false,false,false,-2) }
        fun isSomethingChanged(a: Event, b: Event): Boolean {
            val isWithoutEtui = a.isWithoutEtui.toString() != b.isWithoutEtui.toString()
            val distanceToSchool = a.distanceToSchool != b.distanceToSchool
            val isInSchool = a.isInSchool != b.isInSchool
            val isShouldBeInSchool = a.isShouldBeInSchool != b.isShouldBeInSchool
            val isPhoneHidden = a.isPhoneHidden != b.isPhoneHidden
            val isInMotion = a.isInMotion != b.isInMotion
            return isWithoutEtui || distanceToSchool || isInSchool || isShouldBeInSchool || isPhoneHidden || isInMotion
        }
    }
}