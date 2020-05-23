package com.example.wbudy_apka.location

class Latitude {
    var latitude: Double = 0.0
    constructor() {
        latitude = 0.0
    }
    constructor(stringLatitude: String){
        if (stringLatitude[stringLatitude.length - 1] == 'N') {
            latitude = 1.0
        } else if (stringLatitude[stringLatitude.length - 1] == 'S')
            latitude = -1.0
        val valueString: String = stringLatitude.substring(0,stringLatitude.lastIndex-1);
        val hours = valueString.substring(0,2).toDouble();
        val mins = valueString.substring(2).toDouble();
        latitude *= hours+(mins/60);

    }
    constructor(androidLatitude: Double) {
        latitude = androidLatitude
    }

    override fun toString(): String {
        return "$latitude"
    }

}