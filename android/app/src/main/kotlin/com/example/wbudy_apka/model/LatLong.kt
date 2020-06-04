package com.example.wbudy_apka.model


class LatLong(var latitude: Double, var longtitude: Double) {
    fun degreesToRadians(degrees: Double): Double {
        return degrees*(Math.PI/180.0);
    }
    fun distanceInKilometers(other: LatLong): Double {
        val dlat = degreesToRadians(other.latitude-this.latitude);
        val dlong = degreesToRadians(other.longtitude-this.longtitude);
        val lat1 = degreesToRadians(other.latitude);
        val lat2 = degreesToRadians(this.latitude);
        var long1 = degreesToRadians(other.longtitude);
        var long2 = degreesToRadians(this.latitude);
        val a = 0.5 - Math.cos(dlat)/2 + Math.cos(lat1)*Math.cos(lat2)*(1-Math.cos(dlong))/2;
        return Math.asin(Math.sqrt(a))*12742;
    }
    override fun toString(): String {
        return "LatLong(latitude=$latitude, longtitude=$longtitude)"
    }
}