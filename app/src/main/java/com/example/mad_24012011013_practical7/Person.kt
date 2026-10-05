package com.example.mad_24012011013_practical7

import java.io.Serializable

data class Person(
    val id: Int,
    var name: String,
    var phone: String,
    var email: String,
    var address: String,
    var latitude: String,
    var longitude: String
) : Serializable
