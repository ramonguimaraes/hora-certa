package com.ramonguimaraes.horacerta.domain

interface ProfileRepository {
    suspend fun save(client: Client)
}
