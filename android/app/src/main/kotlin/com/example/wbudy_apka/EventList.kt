package com.example.wbudy_apka

import android.content.Context
import android.content.Context.MODE_APPEND
import java.io.OutputStreamWriter
import kotlin.math.abs

class Event(var timestamp: Long,var isWithoutEtui: ChildState.WithoutEtuiStates, var distanceToSchool: Double, var isInSchool: Boolean, var isShouldBeInSchool: Boolean,var isPhoneHidden: Boolean,var isInMotion: Boolean) {
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

class EventListAppend(private var context: Context) {
    private val EventListFileName = "eventlist.txt"
    private var lastEvent: Event? = null
    private var first = true
    fun appendEvent(event: Event) {
        var ok = false
        if(first) {
            first = false
            ok = true
        }
        if(lastEvent != null) {
            if(Event.isSomethingChanged(lastEvent!!,event)) {
                ok = true
            }
        }
        if(ok) {
            lastEvent = event
            var fileOutputStream= context.openFileOutput(EventListFileName,MODE_APPEND)
            var outputStreamWriter = OutputStreamWriter(fileOutputStream)
            outputStreamWriter.appendln(event.toString())
            outputStreamWriter.close()
            fileOutputStream.close()
        }
    }
}