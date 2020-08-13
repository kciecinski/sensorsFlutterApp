package com.example.wbudy_apka

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import com.example.wbudy_apka.event.Event
import com.example.wbudy_apka.event.EventRepository
import com.example.wbudy_apka.location.Position
import com.example.wbudy_apka.location.PositionListener
import com.example.wbudy_apka.math.Vector3
import com.example.wbudy_apka.model.LatLong
import com.example.wbudy_apka.model.TimeOfDay
import java.lang.Exception

class ChildState(private var context: Context) : PositionListener, SensorEventListener {
    enum class WithoutEtuiStates(var asStr: String) {
        WITH("WITH"),
        WITHOUT("WITHOUT"),
        DO_NOT_HAVE_MAGNETIC("DO_NOT_HAVE_MAGNETIC"),
        DO_NOT_HAVE_ANY("DO_NOT_HAVE_ANY");

        override fun toString(): String {
            return this.asStr;
        }
        companion object {
            fun FromString(str: String): WithoutEtuiStates {
                when(str) {
                    "WITH" -> { return WithoutEtuiStates.WITH }
                    "WITHOUT" -> { return WithoutEtuiStates.WITHOUT }
                    "DO_NOT_HAVE_MAGNETIC" -> { return WithoutEtuiStates.DO_NOT_HAVE_MAGNETIC }
                    "DO_NOT_HAVE_ANY" -> { return WithoutEtuiStates.DO_NOT_HAVE_ANY }
                    else -> {
                        return DO_NOT_HAVE_ANY
                    }
                }
            }
        }
    }

    private lateinit var eventRepository: EventRepository
    private var sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var configuration: Configuration = Configuration(context)

    private var _lastPosition: LatLong = LatLong(0.0,0.0)
    private var lastPosition: LatLong
    get() { return _lastPosition }
    set(value) { _lastPosition = value; dumpEvent()}

    private var _lastMagneticField: Vector3 = Vector3(0.0,0.0,0.0);
    private var lastMagneticField: Vector3
    get() { return _lastMagneticField }
    set(value) { _lastMagneticField = value; dumpEvent()}

    private var _lastProxmity: Double = 0.0;
    private var lastProxmity: Double
    get() { return _lastProxmity }
    set(value) { _lastProxmity = value; dumpEvent()}

    private var _lastAcceleration: Vector3 = Vector3(0.0,0.0,0.0);
    private var lastAcceleration: Vector3
    get() { return _lastAcceleration}
    set(value) { _lastAcceleration = value; dumpEvent()}

    private var _lastRotation: Vector3 = Vector3(0.0,0.0,0.0);
    private var lastRotation: Vector3
    get() { return _lastRotation}
    set(value) { _lastRotation = value; dumpEvent()}

    fun getRadiusCricleForCheckIsInSchool(): Double {
        return 100.0/1000;
    }
    fun getDistanceToSchool(): Double {
        return lastPosition.distanceInKilometers(configuration.getSchoolPosition())
    }
    fun isShouldBeInSchool(): Boolean {
        val now = TimeOfDay.now()
        return now.isInRange(configuration.getSchoolStartAt(),configuration.getSchoolEndAt())
    }
    fun isInSchool(): Boolean {
        return getDistanceToSchool() <= getRadiusCricleForCheckIsInSchool()
    }
    fun isWithoutEtui(): WithoutEtuiStates {
        if(configuration.isHaveEtui())
        {
            if(configuration.getRangeMagneticFieldLengthWithoutEtui().isInRangeInclusive(lastMagneticField.length)){
                return WithoutEtuiStates.WITHOUT
            } else {
                return WithoutEtuiStates.WITH
            }
        } else {
            return WithoutEtuiStates.DO_NOT_HAVE_MAGNETIC
        }
    }
    fun isPhoneHidden(): Boolean {
        return lastProxmity < 0.5
    }
    fun isInMotion(): Boolean {
        val inMotionAcceleration = !configuration.getRangeAccelerationWithoutMotion().isInRangeInclusive(lastAcceleration.length)
        val inMotionGyro = !configuration.getRangeRotationWithoutMotion().isInRangeInclusive(lastRotation.length)
        return inMotionAcceleration || inMotionGyro
    }

    fun dumpEvent() {
        try {
            eventRepository.appendEvent(Event(System.currentTimeMillis(),isWithoutEtui(),getDistanceToSchool(),isInSchool(),isShouldBeInSchool(),isPhoneHidden(),isInMotion()))
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Handler for incomming position
     */
    override fun newPosition(position: Position) {
        this.lastPosition = position.latlong;
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        if(event == null)
            return
        when(event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                lastAcceleration = Vector3(event.values[0].toDouble(),event.values[1].toDouble(),event.values[2].toDouble())
            }
            Sensor.TYPE_GYROSCOPE -> {
                lastRotation = Vector3(event.values[0].toDouble(),event.values[1].toDouble(),event.values[2].toDouble())
            }
            Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED -> {
                lastMagneticField = Vector3(event.values[0].toDouble(),event.values[1].toDouble(),event.values[2].toDouble())
            }
            Sensor.TYPE_LIGHT -> {
                //Log.i("TYPE_LIGHT", event.values.size.toString())
                //Log.i("TYPE_LIGHT",event.values.contentToString())
            }
            Sensor.TYPE_PROXIMITY -> {
                lastProxmity = event.values[0].toDouble();
            }
            else -> {
                Log.e("ChildState","Unimplemented for this sensor");
            }
        }
    }

    fun start() {
        eventRepository = EventRepository(context)
        eventRepository.deleteOld()
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED), SensorManager.SENSOR_DELAY_NORMAL)
        //sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),SensorManager.SENSOR_DELAY_NORMAL)
    }
    fun stop() {
        sensorManager.unregisterListener(this)
    }
}