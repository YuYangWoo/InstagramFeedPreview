package com.example.network.service

import com.example.datasource.InstagramLoginDataSource
import com.example.models.response.TokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface InstagramLoginDataSourceImpl : InstagramLoginDataSource {
    @FormUrlEncoded
    @POST("/oauth/access_token/")
    override suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("code") code: String
    ): TokenResponse

}
