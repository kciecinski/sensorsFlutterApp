package com.example.wbudy_apka.event

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class EventRepositoryHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Event.db"

        private var SQL_CREATE_ENTRIES = "CREATE TABLE ${EventRepositoryEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${EventRepositoryEntry.COLUMN_TIMESTAMP} INTEGER," +
                "${EventRepositoryEntry.COLUMN_IS_WITHOUT_ETUI} TEXT," +
                "${EventRepositoryEntry.COLUMN_DISTANCE_TO_SCHOOL} REAL," +
                "${EventRepositoryEntry.COLUMN_IS_IN_SCHOOL} INTEGER," +
                "${EventRepositoryEntry.COLUMN_IS_SHOULD_BE_IN_SCHOOL} INTEGER," +
                "${EventRepositoryEntry.COLUMN_IS_PHONE_HIDDEN} INTEGER," +
                "${EventRepositoryEntry.COLUMN_IS_IN_MOTION} INTEGER" +
                ")"
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${EventRepositoryEntry.TABLE_NAME}"
    }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
}