package com.example.network.service

import com.example.datasource.GraphInstagramApiServiceSource
import com.example.models.response.BoardDetailResponse
import com.example.models.response.BoardResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GraphInstagramApiServiceSourceImpl : GraphInstagramApiServiceSource {
    @GET("/me/media?fields=id,caption,media_url")
    override suspend fun getBoardInformation(@Query("access_token") accessToken: String, @Query("after") after: String?): BoardResponse

    @GET("/{mediaId}/children?fields=media_url")
    override suspend fun getBoardDetailInformation(@Path("mediaId") mediaId: String, @Query("access_token") accessToken: String): BoardDetailResponse
}