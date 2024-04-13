package com.example.datasource

import com.example.models.response.NetworkShortToken

interface InstagramLoginDataSource {
    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        grantType: String,
        redirectUri: String,
        code: String
    ): NetworkShortToken

}
