package com.example.datasource

import com.example.dto.BoardDTO
import com.example.dto.BoardDetailDTO

interface GraphInstagramApiServiceSource  {
    suspend fun getBoardInformation(accessToken: String, after: String?): BoardDTO
    suspend fun getBoardDetailInformation(mediaId: String, accessToken: String): BoardDetailDTO
}