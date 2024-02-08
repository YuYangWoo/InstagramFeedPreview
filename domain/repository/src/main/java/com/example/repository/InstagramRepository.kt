package com.example.repository

import androidx.paging.PagingData
import com.example.model.BoardEntity
import com.example.model.LoginEntity
import com.example.model.LongTokenEntity
import com.example.model.ShortTokenEntity
import kotlinx.coroutines.flow.Flow

interface InstagramRepository {

    fun fetchShortToken(loginEntity: LoginEntity): Flow<ShortTokenEntity>

    fun fetchLongToken(grantType: String, clientSecret: String, accessToken: String): Flow<LongTokenEntity>

    fun fetchBoardInformation(token: String): Flow<PagingData<BoardEntity.Item>>

}

