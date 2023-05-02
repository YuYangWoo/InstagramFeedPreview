package com.example.repository.remote

import com.example.network.model.request.LoginDTO
import com.example.network.model.response.BoardDTO
import com.example.network.model.response.TokenDTO
import com.example.network.service.GraphInstagramApiService
import com.example.network.service.InstagramApiService
import com.example.repository.InstagramRepository
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
