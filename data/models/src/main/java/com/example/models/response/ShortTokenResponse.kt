package com.example.models.response

import com.example.model.ShortTokenEntity
import com.google.gson.annotations.SerializedName

data class ShortTokenResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("user_id")
    val userId: String
)

fun ShortTokenResponse.toDomain(): ShortTokenEntity {
    return ShortTokenEntity(accessToken, userId)
}
