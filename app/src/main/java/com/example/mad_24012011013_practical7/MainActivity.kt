package com.example.mad_24012011013_practical7

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var db: DatabaseHelper
    private lateinit var adapter: PersonAdapter
    private val people = mutableListOf<Person>()
    private lateinit var progress: ProgressBar
    private lateinit var emptyText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = DatabaseHelper(this)
        val list = findViewById<ListView>(R.id.peopleList)
        progress = findViewById(R.id.progressBar)
        emptyText = findViewById(R.id.emptyText)
        adapter = PersonAdapter(this, people,
            onEdit = { person ->
                startActivity(Intent(this, EditActivity::class.java).apply {
                    putExtra(EditActivity.EXTRA_PERSON, person)
                })
            },
            onDelete = { person ->
                db.delete(person.id)
                loadLocal()
                Toast.makeText(this, "Record deleted", Toast.LENGTH_SHORT).show()
            }
        )
        list.adapter = adapter

        findViewById<Button>(R.id.refreshButton).setOnClickListener { refreshFromApi() }
        list.emptyView = emptyText
    }

    override fun onResume() {
        super.onResume()
        loadLocal()
    }

    private fun loadLocal() {
        people.clear()
        people.addAll(db.getAll())
        adapter.notifyDataSetChanged()
    }

    private fun refreshFromApi() {
        progress.visibility = View.VISIBLE
        Thread {
            try {
                val remotePeople = HttpRequest.fetchPeople()
                remotePeople.forEach(db::upsert)
                runOnUiThread {
                    progress.visibility = View.GONE
                    loadLocal()
                    Toast.makeText(this, "Data refreshed from API", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    progress.visibility = View.GONE
                    Toast.makeText(this, "Unable to load API data: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }
}
