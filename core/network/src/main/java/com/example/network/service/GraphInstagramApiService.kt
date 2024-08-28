package com.example.network.service

import com.example.models.response.NetworkBoardDetail
import com.example.models.response.NetworkBoard
import com.example.models.response.NetworkLongToken
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GraphInstagramApiService {
    @GET("/me/media?fields=id,caption,media_url")
    suspend fun getBoardInformation(@Query("access_token") accessToken: String, @Query("after") after: String?): NetworkBoard

    @GET("/{id}/children?fields=media_url")
    suspend fun getBoardDetailInformation(@Path("id") id: String, @Query("access_token") accessToken: String): NetworkBoardDetail

    @GET("/access_token?")
    suspend fun getAccessLongToken(
        @Query("grant_type") grantType: String,
        @Query("client_secret") clientSecret: String,
        @Query("access_token") accessToken: String
    ): NetworkLongToken
}