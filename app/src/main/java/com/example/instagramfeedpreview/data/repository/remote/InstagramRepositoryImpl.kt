package com.example.instagramfeedpreview.data.repository.remote

import com.example.instagramfeedpreview.data.api.GraphInstagramApiService
import com.example.instagramfeedpreview.data.api.InstagramApiService
import com.example.instagramfeedpreview.data.model.request.LoginDTO
import com.example.instagramfeedpreview.data.model.response.BoardDTO
import com.example.instagramfeedpreview.data.model.response.TokenDTO
import com.example.instagramfeedpreview.domain.repository.InstagramRepository
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
    override fun fetchToken(loginDTO: LoginDTO): Flow<TokenDTO> = flow {
        emit(instagramApiService.getAccessToken(loginDTO.clientId, loginDTO.clientSecret, loginDTO.grantType, loginDTO.redirectUri, loginDTO.code))
    }.flowOn(Dispatchers.IO)


    override fun fetchBoardInformation(
        accessToken: String
    ): Flow<BoardDTO> = flow {
        emit(graphInstagramApiService.getBoardInformation(accessToken))
    }.flowOn(Dispatchers.IO)


}
