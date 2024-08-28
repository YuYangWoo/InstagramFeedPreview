package com.example.network.datasource

import com.example.datasource.GraphInstagramApiServiceSource
import com.example.models.response.NetworkBoard
import com.example.models.response.NetworkBoardDetail
import com.example.models.response.NetworkLongToken
import com.example.network.service.GraphInstagramApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GraphInstagramDataSourceImpl @Inject constructor(
    private val graphInstagramApiService: GraphInstagramApiService
) : GraphInstagramApiServiceSource {
    override suspend fun getBoardInformation(accessToken: String, after: String?): NetworkBoard {
        return graphInstagramApiService.getBoardInformation(accessToken, after)
    }

    override suspend fun getBoardDetailInformation(
        id: String,
        accessToken: String
    ): NetworkBoardDetail {
        return graphInstagramApiService.getBoardDetailInformation(id, accessToken)
    }

    override suspend fun getAccessLongToken(
        grantType: String,
        clientSecret: String,
        accessToken: String
    ): NetworkLongToken {
        return graphInstagramApiService.getAccessLongToken(grantType, clientSecret, accessToken)
    }
}