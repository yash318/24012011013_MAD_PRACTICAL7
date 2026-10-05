package com.example.mad_24012011013_practical7

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var personAdapter: PersonAdapter
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var btnRefresh: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "SQLite and JSON Practical"

        dbHelper = DatabaseHelper(this)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val initialList = dbHelper.allPersons
        personAdapter = PersonAdapter(initialList) { person ->
            dbHelper.deletePerson(person)
            refreshListFromDatabase()
            Toast.makeText(this, "Person deleted", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = personAdapter

        btnRefresh = findViewById(R.id.btn_refresh)
        btnRefresh.setOnClickListener {
            fetchDataFromApi()
        }

        if (initialList.isEmpty()) {
            fetchDataFromApi()
        }
    }

    private fun refreshListFromDatabase() {
        val updatedList = dbHelper.allPersons
        personAdapter.updateData(updatedList)
    }

    private fun fetchDataFromApi() {
        Toast.makeText(this, "Fetching data...", Toast.LENGTH_SHORT).show()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = HttpRequest().makeServiceCall(
                    reqUrl = "https://api.json-generator.com/templates/qjeKFdjkXCdK/data",
                    token = "dchj8v1b6qqdjzbqood1jgpachyfzw58r540gru"
                )
                withContext(Dispatchers.Main) {
                    try {
                        if (data != null) {
                            getPersonDetailsFromJson(data)
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Failed to load data from API",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getPersonDetailsFromJson(jsonString: String) {
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val person = Person.fromJson(jsonObject)
                dbHelper.insertPerson(person)
            }
            refreshListFromDatabase()
            Toast.makeText(this, "Data loaded and saved", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}