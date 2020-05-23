package com.example.wbudy_apka.location

class Longitude {
    var longitude: Double = 0.0
    constructor() {
        longitude = 0.0
    }
    constructor(stringLongitude: String) {
        if (stringLongitude[stringLongitude.length - 1] == 'E') {
            longitude = 1.0
        } else if (stringLongitude[stringLongitude.length - 1] == 'W')
            longitude = -1.0
        val valueString: String = stringLongitude.substring(0,stringLongitude.lastIndex-1);
        val hours = valueString.substring(0,3).toDouble();
        val mins = valueString.substring(3).toDouble();
        longitude *= hours+(mins/60);
    }

    constructor(androidLongitude: Double) {
        longitude = androidLongitude
    }

    override fun toString(): String {
        return "$longitude"
    }

}