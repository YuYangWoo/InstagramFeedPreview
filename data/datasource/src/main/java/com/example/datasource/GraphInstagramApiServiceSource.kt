package com.example.datasource

import com.example.models.response.BoardDetailResponse
import com.example.models.response.BoardResponse
import com.example.models.response.LongTokenResponse

interface GraphInstagramApiServiceSource  {
    suspend fun getBoardInformation(accessToken: String, after: String?): BoardResponse
    suspend fun getBoardDetailInformation(mediaId: String, accessToken: String): BoardDetailResponse
    suspend fun getAccessLongToken(
        grantType: String,
        clientSecret: String,
        accessToken: String,
    ): LongTokenResponse
}