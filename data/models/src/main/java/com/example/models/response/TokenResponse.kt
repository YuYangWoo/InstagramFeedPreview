package com.example.models.response

import com.example.model.Token
import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("user_id")
    val userId: String
)

fun TokenResponse.toDomain(): Token {
    return Token(accessToken, userId)
}
