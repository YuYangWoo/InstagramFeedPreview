package com.example.network.datasource

import com.example.datasource.InstagramLoginDataSource
import com.example.models.response.NetworkShortToken
import com.example.network.service.InstagramLoginService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstagramLoginDataSourceImpl @Inject constructor(
    private val instagramLoginService: InstagramLoginService
) : InstagramLoginDataSource {
    override suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        grantType: String,
        redirectUri: String,
        code: String
    ): NetworkShortToken {
        return instagramLoginService.getAccessToken(clientId, clientSecret, grantType, redirectUri, code)
    }
}