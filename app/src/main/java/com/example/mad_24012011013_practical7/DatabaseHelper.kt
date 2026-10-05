package com.example.mad_24012011013_practical7

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "persons_db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(PersonDbTableData.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + PersonDbTableData.TABLE_NAME)
        onCreate(db)
    }

    fun insertPerson(person: Person): Long {
        val db = writableDatabase
        val values = getValues(person)
        val id = db.insertWithOnConflict(
            PersonDbTableData.TABLE_NAME,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
        db.close()
        return id
    }

    fun insertPerson(name: String, emailId: String, phoneNo: String, address: String): Long {
        val person = Person(
            id = System.currentTimeMillis().toString(),
            name = name,
            emailId = emailId,
            phoneNo = phoneNo,
            address = address,
            latitude = 0.0,
            longitude = 0.0
        )
        return insertPerson(person)
    }

    private fun getValues(person: Person): ContentValues {
        return ContentValues().apply {
            put(PersonDbTableData.COLUMN_ID, person.id)
            put(PersonDbTableData.COLUMN_PERSON_NAME, person.name)
            put(PersonDbTableData.COLUMN_PERSON_EMAIL_ID, person.emailId)
            put(PersonDbTableData.COLUMN_PERSON_PHONE_NO, person.phoneNo)
            put(PersonDbTableData.COLUMN_PERSON_ADDRESS, person.address)
            put(PersonDbTableData.COLUMN_PERSON_GPS_LAT, person.latitude)
            put(PersonDbTableData.COLUMN_PERSON_GPS_LONG, person.longitude)
        }
    }

    fun getPerson(id: String): Person? {
        val db = readableDatabase
        val cursor = db.query(
            PersonDbTableData.TABLE_NAME,
            null,
            "${PersonDbTableData.COLUMN_ID}=?",
            arrayOf(id),
            null, null, null
        )
        var person: Person? = null
        if (cursor.moveToFirst()) {
            person = getPerson(cursor)
        }
        cursor.close()
        return person
    }

    private fun getPerson(cursor: Cursor): Person {
        return Person(
            id = cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_ID)),
            name = cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_NAME)),
            emailId = cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_EMAIL_ID)),
            phoneNo = cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_PHONE_NO)),
            address = cursor.getString(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_ADDRESS)),
            latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_GPS_LAT)),
            longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(PersonDbTableData.COLUMN_PERSON_GPS_LONG))
        )
    }

    val allPersons: ArrayList<Person>
        get() {
            val personList = ArrayList<Person>()
            val selectQuery = "SELECT * FROM ${PersonDbTableData.TABLE_NAME}"
            val db = readableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    personList.add(getPerson(cursor))
                } while (cursor.moveToNext())
            }
            cursor.close()
            return personList
        }

    fun getAllPersons(): List<Person> {
        return allPersons
    }

    val personsCount: Int
        get() {
            val countQuery = "SELECT * FROM ${PersonDbTableData.TABLE_NAME}"
            val db = readableDatabase
            val cursor = db.rawQuery(countQuery, null)
            val count = cursor.count
            cursor.close()
            return count
        }

    fun updatePerson(person: Person): Int {
        val db = writableDatabase
        val values = getValues(person)
        val rows = db.update(
            PersonDbTableData.TABLE_NAME,
            values,
            "${PersonDbTableData.COLUMN_ID}=?",
            arrayOf(person.id)
        )
        db.close()
        return rows
    }

    fun deletePerson(person: Person): Int {
        val db = writableDatabase
        val rows = db.delete(
            PersonDbTableData.TABLE_NAME,
            "${PersonDbTableData.COLUMN_ID}=?",
            arrayOf(person.id)
        )
        db.close()
        return rows
    }
}