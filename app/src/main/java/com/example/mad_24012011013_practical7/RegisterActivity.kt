package com.example.mad_24012011013_practical7

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        dbHelper = DatabaseHelper(this)

        val nameInput = findViewById<EditText>(R.id.name_input)
        val emailInput = findViewById<EditText>(R.id.email_input)
        val phoneInput = findViewById<EditText>(R.id.phone_input)
        val addressInput = findViewById<EditText>(R.id.address_input)
        val saveButton = findViewById<Button>(R.id.save_button)

        saveButton.setOnClickListener {

            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val address = addressInput.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() ||
                phone.isEmpty() || address.isEmpty()
            ) {
                Toast.makeText(
                    this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            dbHelper.insertPerson(
                name,
                email,
                phone,
                address
            )

            Toast.makeText(
                this,
                "Person saved successfully",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
}