package com.example.mad_24012011013_practical7

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE (id INTEGER PRIMARY KEY, name TEXT NOT NULL, phone TEXT, email TEXT, address TEXT, latitude TEXT, longitude TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }

    fun upsert(person: Person) {
        val values = ContentValues().apply {
            put("id", person.id)
            put("name", person.name)
            put("phone", person.phone)
            put("email", person.email)
            put("address", person.address)
            put("latitude", person.latitude)
            put("longitude", person.longitude)
        }
        writableDatabase.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun getAll(): MutableList<Person> {
        val result = mutableListOf<Person>()
        readableDatabase.query(TABLE, null, null, null, null, null, "id ASC").use { cursor ->
            while (cursor.moveToNext()) {
                result.add(
                    Person(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("phone")),
                        cursor.getString(cursor.getColumnIndexOrThrow("email")),
                        cursor.getString(cursor.getColumnIndexOrThrow("address")),
                        cursor.getString(cursor.getColumnIndexOrThrow("latitude")),
                        cursor.getString(cursor.getColumnIndexOrThrow("longitude"))
                    )
                )
            }
        }
        return result
    }

    fun update(person: Person): Int {
        val values = ContentValues().apply {
            put("name", person.name)
            put("phone", person.phone)
            put("email", person.email)
            put("address", person.address)
            put("latitude", person.latitude)
            put("longitude", person.longitude)
        }
        return writableDatabase.update(TABLE, values, "id = ?", arrayOf(person.id.toString()))
    }

    fun delete(id: Int): Int = writableDatabase.delete(TABLE, "id = ?", arrayOf(id.toString()))

    fun clear() = writableDatabase.delete(TABLE, null, null)

    companion object {
        private const val DB_NAME = "people.db"
        private const val DB_VERSION = 1
        private const val TABLE = "people"
    }
}
