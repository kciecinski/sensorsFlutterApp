package com.example.wbudy_apka.model

import java.util.*

class TimeOfDay(var hours: Int, var minutes: Int) {
    companion object {
        fun now(): TimeOfDay {
            val calendar = Calendar.getInstance()
            val time = calendar.time
            val h = time.hours
            val m = time.minutes
            return TimeOfDay(h,m)
        }
    }
    fun isInRange(from: TimeOfDay, to: TimeOfDay): Boolean {
        if( this.hours < from.hours || to.hours < this.hours )
            return false
        else if(this.hours == from.hours) {
            if(this.minutes < from.minutes)
                return false
            return true
        }
        else if(this.hours == to.hours) {
            if(to.minutes < this.hours)
                return false
            return true
        }
        return true
    }

    override fun toString(): String {
        return "TimeOfDay(hours=$hours, minutes=$minutes)"
    }
}