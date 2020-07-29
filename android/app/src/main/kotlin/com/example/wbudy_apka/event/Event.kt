package com.example.wbudy_apka.event

import com.example.wbudy_apka.ChildState

class Event(var timestamp: Long, var isWithoutEtui: ChildState.WithoutEtuiStates, var distanceToSchool: Double, var isInSchool: Boolean, var isShouldBeInSchool: Boolean, var isPhoneHidden: Boolean, var isInMotion: Boolean) {
    override fun toString(): String {
        val x = "\"timestamp\":$timestamp, \"isWithoutEtui\":\"$isWithoutEtui\", \"distanceToSchool\":$distanceToSchool, " +
                "\"isInSchool\":\"$isInSchool\", \"isShouldBeInSchool\":\"$isShouldBeInSchool\", \"isPhoneHidden\":\"$isPhoneHidden\", \"isInMotion\":\"$isInMotion\""
        return "{$x}"
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