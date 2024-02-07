package com.example.datasource

import com.example.models.response.BoardResponse
import com.example.models.response.BoardDetailResponse

interface GraphInstagramApiServiceSource  {
    suspend fun getBoardInformation(accessToken: String, after: String?): BoardResponse
    suspend fun getBoardDetailInformation(mediaId: String, accessToken: String): BoardDetailResponse
}