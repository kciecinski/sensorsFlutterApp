package com.example.wbudy_apka.location

class Latitude {
    constructor(stringLatitude: String){
        if (stringLatitude == "") {
            latitude = ""
        } else {
            latitude = "%.2f".format(stringLatitude.toDouble())
        }
    }
    constructor(androidLatitude: Double) {
        latitude = "%.2f".format(androidLatitude)
    }
    var latitude: String = ""
    override fun toString(): String {
        return "Latitude(nmea='$latitude')"
    }
}