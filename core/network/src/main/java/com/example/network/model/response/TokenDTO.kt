package com.example.network.model.response

import com.google.gson.annotations.SerializedName

data class TokenDTO(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("user_id")
    val userId: String
    )
