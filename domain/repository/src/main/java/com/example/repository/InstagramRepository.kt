package com.example.repository

import androidx.paging.PagingData
import com.example.model.Board
import com.example.model.Login
import com.example.model.LongTokenEntity
import com.example.model.ShortTokenEntity
import kotlinx.coroutines.flow.Flow

interface InstagramRepository {

    fun fetchShortToken(login: Login): Flow<ShortTokenEntity>

    fun fetchLongToken(grantType: String, clientSecret: String, accessToken: String): Flow<LongTokenEntity>

    fun fetchBoardInformation(token: String): Flow<PagingData<Board.Item>>

}

