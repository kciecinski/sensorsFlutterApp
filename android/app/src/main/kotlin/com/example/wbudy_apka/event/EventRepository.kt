package com.example.wbudy_apka.event

import android.content.ContentValues
import android.content.Context
import java.io.OutputStreamWriter


class EventRepository(private var context: Context) {
    private val EventListFileName = "eventlist.txt"
    private var lastEvent: Event? = null
    private var first = true
    private fun appendEventFile(event: Event) {
        var fileOutputStream= context.openFileOutput(EventListFileName,Context.MODE_APPEND)
        var outputStreamWriter = OutputStreamWriter(fileOutputStream)
        outputStreamWriter.appendln(event.toString())
        outputStreamWriter.close()
        fileOutputStream.close()
    }
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
        val newRowId = db?.insertOrThrow(EventRepositoryEntry.TABLE_NAME, null, values)
    }
    private fun boolToInt(v: Boolean): Int {
        if(v)
            return 1
        else
            return 0
    }
}