package com.example.wbudy_apka.location
enum class SVSystemName(var systemName: String) {
    GLONASS("GLONASS"),
    GPS("GPS");

    override fun toString(): String {
        return "SVSystemName(systemName='$systemName')"
    }}
class SVInView(var id: Int?,var Elevation: Int?,var Azimuth: Int?,var SNR: Int?,var system: SVSystemName) {
    override fun toString(): String {
        return "SVInView(id=$id, Elevation=$Elevation, Azimuth=$Azimuth, SNR=$SNR, system=$system)"
    }
}