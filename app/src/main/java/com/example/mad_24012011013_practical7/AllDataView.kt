package com.example.mad_24012011013_practical7


import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AllDataView : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var personAdapter: PersonAdapter
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_all_data_view)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->

            val systemBars =
                insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        dbHelper = DatabaseHelper(this)

        val personList = dbHelper.allPersons

        recyclerView =
            findViewById(R.id.all_data_recycler_view)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        personAdapter =
            PersonAdapter(personList) { person ->
                dbHelper.deletePerson(person)
                personAdapter.updateData(dbHelper.allPersons)
                Toast.makeText(this, "Person deleted", Toast.LENGTH_SHORT).show()
            }

        recyclerView.adapter =
            personAdapter
    }
}