package com.example.mad_24012011013_practical7

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MapActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MapActivity"
    }

    private var lat: Double = 0.0
    private var log: Double = 0.0
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_map)

        @Suppress("DEPRECATION")
        val obj = intent.getSerializableExtra("Object") as? Person

        if (obj != null) {
            Log.i(TAG, "onCreate: Object:$obj")
            lat = obj.latitude
            log = obj.longitude
            title = obj.name

            findViewById<TextView>(R.id.name_text).text = title
            findViewById<TextView>(R.id.address_text).text = obj.address
            findViewById<TextView>(R.id.lat_text).text = "Latitude: $lat"
            findViewById<TextView>(R.id.long_text).text = "Longitude: $log"
        }
    }
}