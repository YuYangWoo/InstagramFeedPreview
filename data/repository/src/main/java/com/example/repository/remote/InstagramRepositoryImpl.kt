package com.example.repository.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.datasource.BoardLocalDataSource
import com.example.datasource.BoardPagingSource
import com.example.datasource.GraphInstagramApiServiceSource
import com.example.datasource.InstagramLoginDataSource
import com.example.dto.toDomain
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
class InstagramRepositoryImpl @Inject constructor(
    private val instagramLoginDataSource: InstagramLoginDataSource,
    private val graphInstagramApiServiceSource: GraphInstagramApiServiceSource,
    private val boardLocalDataSource: BoardLocalDataSource,
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

    override fun fetchBoardInformation(accessToken: String, after: String?) =
        Pager(
            config = PagingConfig(pageSize = 25, enablePlaceholders = false),
            pagingSourceFactory = { BoardPagingSource(graphInstagramApiServiceSource, boardLocalDataSource, accessToken) }
        ).flow

}
