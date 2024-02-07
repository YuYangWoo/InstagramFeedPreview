package com.example.repository.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.datasource.BoardLocalDataSource
import com.example.datasource.BoardPagingSource
import com.example.datasource.GraphInstagramApiServiceSource
import com.example.datasource.InstagramLoginDataSource
import com.example.datasource.UserDataStoreSource
import com.example.model.BoardEntity
import com.example.model.LoginEntity
import com.example.model.LongTokenEntity
import com.example.model.ShortTokenEntity
import com.example.models.response.toDomain
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
    private val userDataStoreSource: UserDataStoreSource
) : InstagramRepository {

    override fun fetchShortToken(loginEntity: LoginEntity): Flow<ShortTokenEntity> = flow {
        emit(
            instagramLoginDataSource.getAccessToken(
                loginEntity.clientId,
                loginEntity.clientSecret,
                loginEntity.grantType,
                loginEntity.redirectUri,
                loginEntity.code
            ).toDomain()
        )
    }.flowOn(Dispatchers.IO)

    override fun fetchLongToken(grantType: String, clientSecret: String, accessToken: String): Flow<LongTokenEntity> = flow {
        emit(
            graphInstagramApiServiceSource.getAccessLongToken(
                grantType,
                clientSecret,
                accessToken,
            ).toDomain()
        )
    }

    override fun fetchBoardInformation(token: String): Flow<PagingData<BoardEntity.Item>> {
        return Pager(
            config = PagingConfig(pageSize = 25),
            pagingSourceFactory = { BoardPagingSource(graphInstagramApiServiceSource, boardLocalDataSource, token) }
        ).flow
    }

}
