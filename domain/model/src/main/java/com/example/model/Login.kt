package com.example.model

import java.io.Serializable

data class Login(
    val clientId: String,
    val clientSecret: String,
    val grantType: String,
    val redirectUri: String,
    val code: String
) : Serializable
