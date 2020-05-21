package com.example.wbudy_apka.location

class Longitude {
    constructor(stringLongitude: String) {
        if (stringLongitude == "") {
            longitude = ""
        } else {
            longitude = "%.2f".format(stringLongitude.toDouble())
        }
    }
    constructor(androidLongitude: Double) {
        longitude = "%.2f".format(androidLongitude)
    }
    var longitude: String = ""
    override fun toString(): String {
        return "Longitude(nmea='$longitude')"
    }
}