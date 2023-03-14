package com.example.instagramfeedpreview.data.api

import com.example.instagramfeedpreview.data.model.response.BoardDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface GraphInstagramApiService {
    @GET("/me/media?fields=id,caption,media_url")
    suspend fun getBoardInformation(@Query("access_token") accessToken: String): BoardDTO
}
