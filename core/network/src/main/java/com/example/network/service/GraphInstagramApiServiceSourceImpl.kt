package com.example.network.service

import com.example.datasource.GraphInstagramApiServiceSource
import com.example.models.response.BoardDetailResponse
import com.example.models.response.BoardResponse
import com.example.models.response.LongTokenResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GraphInstagramApiServiceSourceImpl : GraphInstagramApiServiceSource {
    @GET("/me/media?fields=id,caption,media_url")
    override suspend fun getBoardInformation(@Query("access_token") accessToken: String, @Query("after") after: String?): BoardResponse

    @GET("/{mediaId}/children?fields=media_url")
    override suspend fun getBoardDetailInformation(@Path("mediaId") mediaId: String, @Query("access_token") accessToken: String): BoardDetailResponse

    @GET("/access_token?")
    override suspend fun getAccessLongToken(
        @Query("grant_type") grantType: String,
        @Query("client_secret") clientSecret: String,
        @Query("access_token") accessToken: String
    ): LongTokenResponse
}