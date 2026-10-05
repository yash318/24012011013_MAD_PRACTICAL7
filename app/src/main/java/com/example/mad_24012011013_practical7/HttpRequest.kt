package com.example.mad_24012011013_practical7

import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

object HttpRequest {
    private const val API_URL = "https://jsonplaceholder.typicode.com/users"

    fun fetchPeople(): List<Person> {
        val connection = (URL(API_URL).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 10000
            readTimeout = 10000
        }
        return try {
            val text = connection.inputStream.bufferedReader().use { it.readText() }
            val array = JSONArray(text)
            buildList {
                for (i in 0 until array.length()) {
                    val item = array.getJSONObject(i)
                    val addressObject = item.optJSONObject("address")
                    val geo = addressObject?.optJSONObject("geo")
                    add(
                        Person(
                            id = item.getInt("id"),
                            name = item.optString("name"),
                            phone = item.optString("phone"),
                            email = item.optString("email"),
                            address = addressObject?.optString("street").orEmpty() + ", " + addressObject?.optString("city").orEmpty(),
                            latitude = geo?.optString("lat").orEmpty(),
                            longitude = geo?.optString("lng").orEmpty()
                        )
                    )
                }
            }
        } finally {
            connection.disconnect()
        }
    }
}
