package com.example.models.response

import com.example.model.LongToken
import com.google.gson.annotations.SerializedName

data class NetworkLongToken(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_type") val tokenType: String,
    @SerializedName("expires_in") val expiresIn: String,
)

fun NetworkLongToken.toDomain(): LongToken {
    return LongToken(accessToken, tokenType, expiresIn)
}