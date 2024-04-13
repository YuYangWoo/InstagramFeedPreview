package com.example.models.response

import com.example.model.ShortToken
import com.google.gson.annotations.SerializedName

data class NetworkShortToken(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("user_id")
    val userId: String
)

fun NetworkShortToken.toDomain(): ShortToken {
    return ShortToken(accessToken, userId)
}
