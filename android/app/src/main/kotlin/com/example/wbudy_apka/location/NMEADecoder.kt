package com.example.wbudy_apka.location

import android.icu.util.TimeZone
import java.util.*
import kotlin.collections.ArrayList
import kotlin.experimental.xor

class NMEADecoder {
    private val regexHeader: Regex = "^(?:\\\$)([^,]{1,})".toRegex(RegexOption.MULTILINE)
    private var state = NmeaState()

    private var positionListeners: ArrayList<PositionListener> = ArrayList<PositionListener>()
    fun addPositionListener(listener: PositionListener) {
        positionListeners.add(listener)
    }
    fun removePositionListener(listener: PositionListener) {
        positionListeners.remove(listener)
    }

    private var _position: Position = Position()
    private var position: Position
        get() { return _position }
        set(value) {
            positionAvailable = true
            _position = Position.mergePosition(_position,value)
            for (positionListener in positionListeners) {
                positionListener.newPosition(position)
            }
        }

    private var positionAvailable: Boolean = false;
    fun isPositionAvailable(): Boolean {
        return positionAvailable;
    }

    fun decodeString(nmea: String) {
        val resultHeader = regexHeader.find(nmea);
        if (resultHeader != null) {
            val header = resultHeader.groups.get(1)?.value
            when(header) {
                "GPGGA" -> decodeStringGPGGA(nmea)
                "GPRMC" -> decodeStringGPRMC(nmea)
                "GPVTG" -> { }
                "GPGSV" -> decodeStringGPGSV(nmea)
                "GLGSV" -> decodeStringGLGSV(nmea)
                "GPGSA" -> decodeStringGPGSA(nmea)
                "GNGSA" -> decodeStringGNGSA(nmea)
                else -> {
                    println("Unsupported: "+nmea)
                }
            }
        }
        if(state.getMessageCounter() == state.getTotalMessageCounter()) {
            for(sv in state.getAllSVInView()){
                println(sv)
            }
        }
    }
    private fun decodeStringGPGGA(nmea: String) {
        val x = NmeaGPGGA(nmea,state)
        if(!x.isValid())
            return
        val longitude = Longitude(x.getLongitude() + x.getLongitudeLetter())
        val latitude = Latitude(x.getLatitude() + x.getLatitudeLetter())
        val hours = x.getUTC().substring(0, 2).toLong()
        val minutes = x.getUTC().substring(2, 4).toLong()
        val seconds = x.getUTC().substring(4, 6).toLong()
        val miliseconds = x.getUTC().substring(7).toLong()
        var dateTime: Long = hours
        dateTime = (dateTime*60)+minutes
        dateTime = (dateTime*60)+seconds
        dateTime = (dateTime*1000)+miliseconds
        if(x.getUnitAltitude() == "M"){
            val altitude = x.getAltitude().toDouble()
            position = Position(latitude,longitude,dateTime,altitude)
        }else{
            position = Position(latitude,longitude,dateTime)
        }
    }
    private fun decodeStringGPRMC(nmea: String) {
        val x = NmeaGPRMC(nmea, state)
        if (!x.isValid())
            return
        val longitude = Longitude(x.getLongitude() + x.getLongitudeLetter())
        val latitude = Latitude(x.getLatitude() + x.getLatitudeLetter())
        val hours = x.getUTC().substring(0, 2).toLong()
        val minutes = x.getUTC().substring(2, 4).toLong()
        val seconds = x.getUTC().substring(4, 6).toLong()
        val miliseconds = x.getUTC().substring(7).toLong()
        var dateTime: Long = hours
        dateTime = (dateTime*60)+minutes
        dateTime = (dateTime*60)+seconds
        dateTime = (dateTime*1000)+miliseconds
        position = Position(latitude, longitude, dateTime)
    }
    private fun decodeStringGPGSV(nmea: String) {
        val x = NmeaGPGSV(nmea,state)
        if(!x.isValid())
            return
        state.GPGSVTotalMessageCounter = x.getTotalNumberOfMessagesInThisCycle()
        if(state.getGPGSVNextMessageNumber() == 1) {
            state.GPGSVSatelitesInView.clear()
        }
        if (state.getGPGSVNextMessageNumber() == x.getMessageNumber()) {
            state.GPGSVMessageCounter = x.getMessageNumber()
        }
        for(sv in x.getAllSVInView()) {
            state.GPGSVSatelitesInView.add(sv)
        }

    }
    private fun decodeStringGLGSV(nmea: String) {
        val x = NmeaGLGSV(nmea,state)
        if(!x.isValid())
            return

        state.GLGSVTotalMessageCounter = x.getTotalNumberOfMessagesInThisCycle()
        if(state.getGLGSVNextMessageNumber() == 1) {
            state.GLGSVSatelitesInView.clear()
        }
        if (state.getGLGSVNextMessageNumber() == x.getMessageNumber()) {
            state.GLGSVMessageCounter = x.getMessageNumber()
        }
        for(sv in x.getAllSVInView()) {
            state.GLGSVSatelitesInView.add(sv)
        }
    }
    private fun decodeStringGPGSA(nmea: String) {
        val x = NmeaGPGSA(nmea,state)
        if(!x.isValid())
            return
        if(x.getFixPositionMode() == x.FixNotAvailable)
            return
        println("Unimplemented: GPGSA")
    }
    private fun decodeStringGNGSA(nmea: String) {
        val x = NmeaGNGSA(nmea,state)
        if(!x.isValid())
            return
        if(x.getFixPositionMode() == x.FixNotAvailable)
            return
        println("Unimplemented: GNGSA")
    }
}

open class Nmea(nmea: String,_state: NmeaState) {
    var msgString = nmea
    var msg: List<String> = nmea.split(',');
    var state: NmeaState = _state
    open fun isValid(): Boolean {
        return isChecksumValid()
    }
    fun getChecksum(): String {
        return msg.get(msg.size-1).substring(1);
    }
    fun calcChecksum(): String {
        val str = msgString.substring(1,msgString.lastIndex-2)
        var sum: Byte = str.get(0).toByte()
        for (i in IntRange(1,str.lastIndex))
            sum = sum.xor(str.get(i).toByte())
        return String.format("%02X", sum)
    }
    fun isChecksumValid(): Boolean {
        return (getChecksum() == calcChecksum())
    }
}

class NmeaGPGGA(nmea: String,state: NmeaState) : Nmea(nmea,state) {
    val GPSQualityIndicator_Invalid = "0"
    val GPSQualityIndicator_GpsFix = "1"
    val GPSQualityIndicator_DiffGpsFix = "2"
    override fun isValid(): Boolean {
        return (GPSQualityIndicator_Invalid != getGPSQualityIndicator()) && super.isValid()
    }
    fun getUTC(): String {
        return msg.get(1)
    }
    fun getLatitude(): String {
        return msg.get(2)
    }
    fun getLatitudeLetter(): Char {
        return msg.get(3).get(0);
    }
    fun getLongitude(): String {
        return msg.get(4)
    }
    fun getLongitudeLetter(): Char {
        return msg.get(5).get(0)
    }
    fun getGPSQualityIndicator(): String {
        return msg.get(6);
    }
    fun getAltitude(): String {
        return msg.get(9)
    }
    fun getUnitAltitude(): String {
        return msg.get(10)
    }
}

class NmeaGPRMC(nmea: String,state: NmeaState) : Nmea(nmea,state) {
    val Data_Valid = "A"
    val Data_Invalid = "V"
    override fun isValid(): Boolean {
        return (Data_Valid == getDataValid())
    }
    fun getUTC(): String {
        return msg.get(1)
    }
    fun getDataValid(): String {
        return msg.get(2)
    }
    fun getLatitude(): String {
        return msg.get(3)
    }
    fun getLatitudeLetter(): Char {
        return msg.get(4).get(0);
    }
    fun getLongitude(): String {
        return msg.get(5)
    }
    fun getLongitudeLetter(): Char {
        return msg.get(6).get(0)
    }
    fun getSpeedInKnots(): Float {
        return msg.get(7).toFloat()
    }
    fun getDateStamp(): String {
        return msg.get(8)
    }
}

open class NmeaGPGSV(nmea: String,state: NmeaState) : Nmea(nmea,state) {
    override fun isValid(): Boolean {
        val cond1 = (1 <= getMessageNumber() && getMessageNumber() <= getTotalNumberOfMessagesInThisCycle())
        val cond2 = (getMessageNumber() == state.getGPGSVNextMessageNumber())
        return super.isValid() && (cond1 && cond2)
    }
    fun getTotalNumberOfMessagesInThisCycle(): Int {
        return msg.get(1).toInt()
    }
    fun getMessageNumber(): Int {
        return msg.get(2).toInt()
    }
    fun getTotalNumberSVinView(): Int {
        return msg.get(3).toInt()
    }
    open fun getAllSVInView(): List<SVInView> {
        val l: MutableList<SVInView> = ArrayList<SVInView>(0)
        for(i in IntRange(4,msg.size) step 4) {
            if( i+3 >= msg.size )
                break
            val id = msg.get(i).toIntOrNull()
            val elevation = msg.get(i+1).toIntOrNull()
            val azimuth = msg.get(i+2).toIntOrNull()
            val snr = msg.get(i+3).toIntOrNull()
            val sv = SVInView(id,elevation,azimuth,snr,SVSystemName.GPS)
            l.add(sv)
        }
        return l
    }
}

class NmeaGLGSV(nmea: String,state: NmeaState) : NmeaGPGSV(nmea,state) {
    override fun isValid(): Boolean {
        val cond1 = (1 <= getMessageNumber() && getMessageNumber() <= getTotalNumberOfMessagesInThisCycle())
        val cond2 = (getMessageNumber() == (state.getGLGSVNextMessageNumber()))
        return super.isChecksumValid() && (cond1 && cond2)
    }
    override fun getAllSVInView(): List<SVInView> {
        val l: MutableList<SVInView> = ArrayList<SVInView>(0)
        for(i in IntRange(4,msg.size) step 4) {
            if( i+3 >= msg.size )
                break
            val id = msg.get(i).toIntOrNull()
            val elevation = msg.get(i+1).toIntOrNull()
            val azimuth = msg.get(i+2).toIntOrNull()
            val snr = msg.get(i+3).toIntOrNull()
            val sv = SVInView(id,elevation,azimuth,snr,SVSystemName.GLONASS)
            l.add(sv)
        }
        return l
    }
}

open class NmeaGPGSA(nmea: String, state: NmeaState) : Nmea(nmea,state) {
    val FixNotAvailable = "1"
    val Fix2D = "2"
    val Fix3D = "3"
    fun getFixPositionMode(): String {
        return msg.get(2)
    }
}

class NmeaGNGSA(nmea: String,state: NmeaState): NmeaGPGSA(nmea,state) {

}