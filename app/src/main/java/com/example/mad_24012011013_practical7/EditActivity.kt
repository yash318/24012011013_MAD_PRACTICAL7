package com.example.mad_24012011013_practical7

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditActivity : AppCompatActivity() {
    companion object { const val EXTRA_PERSON = "person" }

    private lateinit var db: DatabaseHelper
    private lateinit var person: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        db = DatabaseHelper(this)
        person = intent.getSerializableExtra(EXTRA_PERSON) as? Person
            ?: run { finish(); return }

        findViewById<EditText>(R.id.nameInput).setText(person.name)
        findViewById<EditText>(R.id.phoneInput).setText(person.phone)
        findViewById<EditText>(R.id.emailInput).setText(person.email)
        findViewById<EditText>(R.id.addressInput).setText(person.address)
        findViewById<EditText>(R.id.latitudeInput).setText(person.latitude)
        findViewById<EditText>(R.id.longitudeInput).setText(person.longitude)

        findViewById<Button>(R.id.saveButton).setOnClickListener {
            val updated = person.copy(
                name = findViewById<EditText>(R.id.nameInput).text.toString().trim(),
                phone = findViewById<EditText>(R.id.phoneInput).text.toString().trim(),
                email = findViewById<EditText>(R.id.emailInput).text.toString().trim(),
                address = findViewById<EditText>(R.id.addressInput).text.toString().trim(),
                latitude = findViewById<EditText>(R.id.latitudeInput).text.toString().trim(),
                longitude = findViewById<EditText>(R.id.longitudeInput).text.toString().trim()
            )
            if (updated.name.isEmpty()) {
                Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            db.update(updated)
            Toast.makeText(this, "Record updated", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
