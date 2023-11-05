package com.example.repository.remote

import com.example.datasource.GraphInstagramApiServiceSource
import com.example.datasource.InstagramLoginDataSource
import com.example.dto.toDomain
import com.example.model.Board
import com.example.model.Login
import com.example.model.Token
import com.example.repository.InstagramRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
public class InstagramRepositoryImpl @Inject constructor(
    private val instagramLoginDataSource: InstagramLoginDataSource,
    private val graphInstagramApiServiceSource: GraphInstagramApiServiceSource
) : InstagramRepository {

    override fun fetchToken(login: Login): Flow<Token> = flow {
        emit(
            instagramLoginDataSource.getAccessToken(
                login.clientId,
                login.clientSecret,
                login.grantType,
                login.redirectUri,
                login.code
            ).toDomain()
        )
    }.flowOn(Dispatchers.IO)

    override fun fetchBoardInformation(accessToken: String): Flow<Board> = flow {
        emit(graphInstagramApiServiceSource.getBoardInformation(accessToken).toDomain())
    }.flowOn(Dispatchers.IO)

}
