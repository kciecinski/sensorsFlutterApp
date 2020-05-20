package com.example.wbudy_apka.location

class Longitude {
    constructor(stringLongitude: String) {
        longitude = stringLongitude
    }
    constructor(androidLongitude: Double) {
        longitude = androidLongitude.toString()
    }
    var longitude: String = ""
    override fun toString(): String {
        return "Longitude(nmea='$longitude')"
    }
}