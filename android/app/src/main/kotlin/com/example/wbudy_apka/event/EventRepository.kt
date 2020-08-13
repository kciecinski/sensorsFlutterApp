package com.example.wbudy_apka.event

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import com.example.wbudy_apka.ChildState
import java.io.OutputStreamWriter


class EventRepository(private var context: Context) {
    fun deleteOld() {
        val dbHelper = EventRepositoryHelper(context)
        val db = dbHelper.writableDatabase
        val lastEventId = getLastEvent().id
        val deleteToId = lastEventId - 200;
        db.execSQL("DELETE FROM ${EventRepositoryEntry.TABLE_NAME} WHERE ${BaseColumns._ID} < ${deleteToId}")
        db.close()
        dbHelper.close()
    }
    private var lastEvent: Event? = null
    private var first = true
    fun appendEvent(event: Event) {
        var ok = false
        if(first) {
            first = false
            ok = true
        }
        if(lastEvent != null) {
            if(Event.isSomethingChanged(lastEvent!!,event)) {
                ok = true
            }
        }
        if(ok) {
            lastEvent = event
            //appendEventFile(event)
            appendEventDB(event)
        }
    }
    fun getNextEventById(id: Long): Event {
        val dbHelper = EventRepositoryHelper(context)
        val db = dbHelper.writableDatabase

        var result = db.rawQuery("SELECT MIN(${BaseColumns._ID}) FROM ${EventRepositoryEntry.TABLE_NAME} WHERE ${BaseColumns._ID} > ${id}", null)
        result.moveToLast()
        val id = result.getLong(0)
        result.close()
        db.close()
        dbHelper.close()
        return getEventById(id)
    }
    fun getPrevEventById(id: Long): Event {
        val dbHelper = EventRepositoryHelper(context)
        val db = dbHelper.writableDatabase

        var result = db.rawQuery("SELECT MAX(${BaseColumns._ID}) FROM ${EventRepositoryEntry.TABLE_NAME} WHERE ${BaseColumns._ID} < ${id}", null)
        result.moveToLast()
        val id = result.getLong(0)
        result.close()
        db.close()
        dbHelper.close()
        return getEventById(id)
    }
    fun getLastEvent(): Event {
        val dbHelper = EventRepositoryHelper(context)
        val db = dbHelper.writableDatabase

        var result = db.rawQuery("SELECT MAX(${BaseColumns._ID}) FROM ${EventRepositoryEntry.TABLE_NAME}", null)
        result.moveToLast()
        val id = result.getLong(0)
        result.close()
        db.close()
        dbHelper.close()
        return getEventById(id)
    }
    fun getEventById(id: Long): Event {
        val dbHelper = EventRepositoryHelper(context)
        val db = dbHelper.writableDatabase

        val projection = arrayOf(BaseColumns._ID,
                EventRepositoryEntry.COLUMN_TIMESTAMP,
                EventRepositoryEntry.COLUMN_IS_WITHOUT_ETUI,
                EventRepositoryEntry.COLUMN_IS_SHOULD_BE_IN_SCHOOL,
                EventRepositoryEntry.COLUMN_IS_PHONE_HIDDEN,
                EventRepositoryEntry.COLUMN_DISTANCE_TO_SCHOOL,
                EventRepositoryEntry.COLUMN_IS_IN_MOTION,
                EventRepositoryEntry.COLUMN_IS_IN_SCHOOL)
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(id.toString())
        val sortOrder = "${BaseColumns._ID} ASC"

        val result = db.query(EventRepositoryEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder)
        if(result.count < 1) {
            val event = Event.invalidEvent
            result.close()
            db.close()
            dbHelper.close()
            return event
        } else {
            result.moveToFirst()
            val event = Event(
                    result.getLong(1),
                    ChildState.WithoutEtuiStates.FromString(result.getString(2)),
                    result.getDouble(5),
                    intToBool(result.getInt(7)),
                    intToBool(result.getInt(3)),
                    intToBool(result.getInt(4)),
                    intToBool(result.getInt(6)),
                    result.getLong(0)
            )
            result.close()
            db.close()
            dbHelper.close()
            return event
        }
    }
    private fun appendEventDB(event: Event) {
        val dbHelper = EventRepositoryHelper(context)
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(EventRepositoryEntry.COLUMN_TIMESTAMP,event.timestamp)
            put(EventRepositoryEntry.COLUMN_IS_WITHOUT_ETUI,event.isWithoutEtui.toString())
            put(EventRepositoryEntry.COLUMN_DISTANCE_TO_SCHOOL, event.distanceToSchool)
            put(EventRepositoryEntry.COLUMN_IS_IN_SCHOOL,boolToInt(event.isInSchool))
            put(EventRepositoryEntry.COLUMN_IS_SHOULD_BE_IN_SCHOOL,boolToInt(event.isShouldBeInSchool))
            put(EventRepositoryEntry.COLUMN_IS_PHONE_HIDDEN,boolToInt(event.isPhoneHidden))
            put(EventRepositoryEntry.COLUMN_IS_IN_MOTION,boolToInt(event.isInMotion))
        }
        db?.insertOrThrow(EventRepositoryEntry.TABLE_NAME, null, values)
        db.close()
        dbHelper.close()
    }
    private fun boolToInt(v: Boolean): Int {
        if(v)
            return 1
        else
            return 0
    }
    private fun intToBool(v: Int): Boolean {
        if(v == 1)
            return true
        else
            return false
    }
}