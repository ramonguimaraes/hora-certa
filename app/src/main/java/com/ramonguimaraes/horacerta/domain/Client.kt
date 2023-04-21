package com.ramonguimaraes.horacerta.domain

data class Client(
    val id: String,
    val name: String,
    val email: String
)

fun Client.toHashMap(): HashMap<String, String> {
    return hashMapOf(
        "id" to id,
        "name" to name,
        "email" to email
    )
}
