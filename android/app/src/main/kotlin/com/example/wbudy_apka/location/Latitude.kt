package com.example.wbudy_apka.location

class Latitude {
    constructor(stringLatitude: String){
        latitude = stringLatitude
    }
    constructor(androidLatitude: Double) {
        latitude = androidLatitude.toString()
    }
    var latitude: String = ""
    override fun toString(): String {
        return "Latitude(nmea='$latitude')"
    }
}