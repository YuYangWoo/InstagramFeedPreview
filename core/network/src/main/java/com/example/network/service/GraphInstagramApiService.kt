package com.example.network.service

import com.example.dto.BoardChildDTO
import com.example.dto.BoardDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GraphInstagramApiService {
    @GET("/me/media?fields=id,caption,media_url")
    suspend fun getBoardInformation(@Query("access_token") accessToken: String): BoardDTO

    @GET("/{mediaId}/children?fields=media_url")
    suspend fun getBoardChildInformation(@Path("mediaId") mediaId: String, @Query("access_token") accessToken: String): BoardChildDTO
}
