package com.example.wbudy_apka.location

import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

public class Position {
    companion object {
        fun mergePosition(older: Position, newer: Position): Position {
            if(!older.isValid())
                return newer
            if(!newer.availableAltitude && older.availableAltitude) {
                return Position(newer.latitude,newer.longitude,newer.datetime,older.altitude)
            }
            return newer
        }
    }
    private var valid: Boolean = false;
    fun isValid(): Boolean {
        return valid
    }

    var altitude: Double = 0.0
    var availableAltitude: Boolean = false
    var latitude: Latitude = Latitude()
    var longitude: Longitude = Longitude()
    var datetime: Long = 0
    constructor() {
        valid = false
    }
    constructor(latitude: Latitude,longitude: Longitude,datetime: Long) {
        valid = true
        this.latitude = latitude
        this.longitude = longitude
        this.datetime = datetime
        availableAltitude = false
    }
    constructor(latitude: Latitude,longitude: Longitude,datetime: Long,altitude: Double) {
        valid = true
        this.latitude = latitude
        this.longitude = longitude
        this.datetime = datetime
        availableAltitude = true
        this.altitude = altitude
    }

    override fun toString(): String {
        if(availableAltitude)
            return "Position(altitude=$altitude latitude=$latitude, longitude=$longitude, datetime=$datetime)"
        else
            return "Position(latitude=$latitude, longitude=$longitude, time=$datetime)"
    }


    //override fun toString(): String {
    //}

}