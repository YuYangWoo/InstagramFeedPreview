package com.example.dto

import com.example.model.Login

public data class LoginDTO(
    val clientId: String,
    val clientSecret: String,
    val grantType: String,
    val redirectUri: String,
    val code: String
)
public fun LoginDTO.toDomain(): Login {
    return Login(clientId, clientSecret, grantType, redirectUri, code)
}
