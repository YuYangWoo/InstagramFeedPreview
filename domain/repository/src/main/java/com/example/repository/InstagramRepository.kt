package com.example.repository

import androidx.paging.PagingData
import com.example.model.Board
import com.example.model.Login
import com.example.model.LongToken
import com.example.model.ShortToken
import kotlinx.coroutines.flow.Flow

interface InstagramRepository {

    suspend fun fetchShortToken(login: Login): ShortToken

    suspend fun fetchLongToken(grantType: String, clientSecret: String, accessToken: String): LongToken

    fun fetchBoardInformation(token: String): Flow<PagingData<Board.Item>>

}

