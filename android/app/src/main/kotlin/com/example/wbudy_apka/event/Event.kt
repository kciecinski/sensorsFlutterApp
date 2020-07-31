package com.example.wbudy_apka.event

import com.example.wbudy_apka.ChildState
import java.util.*
import kotlin.collections.HashMap

class Event(var timestamp: Long, var isWithoutEtui: ChildState.WithoutEtuiStates,
            var distanceToSchool: Double, var isInSchool: Boolean,
            var isShouldBeInSchool: Boolean, var isPhoneHidden: Boolean,
            var isInMotion: Boolean, var id: Long = -1) {
    override fun toString(): String {
        val x = "\"timestamp\":$timestamp, \"isWithoutEtui\":\"$isWithoutEtui\", \"distanceToSchool\":$distanceToSchool, " +
                "\"isInSchool\":\"$isInSchool\", \"isShouldBeInSchool\":\"$isShouldBeInSchool\", \"isPhoneHidden\":\"$isPhoneHidden\", \"isInMotion\":\"$isInMotion\""
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