package com.example.dto

import com.example.model.Login

data class login(
    val clientId: String,
    val clientSecret: String,
    val grantType: String,
    val redirectUri: String,
    val code: String
) {
    fun login.toDomain(): Login {
        return Login(clientId, clientSecret, grantType, redirectUri, code)
    }
}
