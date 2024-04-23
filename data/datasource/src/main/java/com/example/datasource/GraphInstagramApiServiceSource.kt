package com.example.datasource

import com.example.models.response.NetworkBoardDetail
import com.example.models.response.NetworkBoard
import com.example.models.response.NetworkLongToken

interface GraphInstagramApiServiceSource  {
    suspend fun getBoardInformation(accessToken: String, after: String?): NetworkBoard
    suspend fun getBoardDetailInformation(id: String, accessToken: String): NetworkBoardDetail
    suspend fun getAccessLongToken(
        grantType: String,
        clientSecret: String,
        accessToken: String,
    ): NetworkLongToken
}