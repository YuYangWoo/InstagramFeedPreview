package com.example.instagramfeedpreview.data.api

import com.example.instagramfeedpreview.data.model.response.TokenDTO
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface InstagramApiService {
    @FormUrlEncoded
    @POST("/oauth/access_token/")
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("code") code: String
    ): TokenDTO

}
