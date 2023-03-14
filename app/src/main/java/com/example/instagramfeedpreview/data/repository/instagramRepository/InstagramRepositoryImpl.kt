package com.example.instagramfeedpreview.data.repository.instagramRepository

import com.example.instagramfeedpreview.data.api.GraphInstagramApiService
import com.example.instagramfeedpreview.data.api.InstagramApiService
import com.example.instagramfeedpreview.data.model.response.BoardDTO
import com.example.instagramfeedpreview.data.model.response.TokenDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstagramRepositoryImpl @Inject constructor(
    private val instagramApiService: InstagramApiService,
    private val graphInstagramApiService: GraphInstagramApiService
) : InstagramRepository {
    override fun fetchToken(clientId: String,
                            clientSecret: String,
                            grantType: String,
                            redirectUri: String,
                            code: String): Flow<TokenDTO> = flow {
        emit(instagramApiService.getAccessToken(clientId, clientSecret, grantType, redirectUri, code))
    }.flowOn(Dispatchers.IO)


    override fun fetchBoardInformation(
        accessToken: String
    ): Flow<BoardDTO> = flow {
        emit(graphInstagramApiService.getBoardInformation(accessToken))
    }.flowOn(Dispatchers.IO)


}
interface InstagramRepository {
    fun fetchToken(clientId: String,
                   clientSecret: String,
                   grantType: String,
                   redirectUri: String,
                   code: String): Flow<TokenDTO>

    fun fetchBoardInformation(
        accessToken: String
    ): Flow<BoardDTO>
}
