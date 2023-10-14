package com.example.dto

import com.example.model.Token
import com.google.gson.annotations.SerializedName

data class TokenDTO(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("user_id")
    val userId: String
)

fun TokenDTO.toDomain(): Token {
    return Token(accessToken, userId)
}