package com.example.datasource

import com.example.dto.BoardDTO
import com.example.dto.BoardDetailDTO

interface GraphInstagramApiServiceSource  {
    suspend fun getBoardInformation(accessToken: String): BoardDTO
    suspend fun getBoardChildInformation(mediaId: String, accessToken: String): BoardDetailDTO
}