package com.example.repository

import androidx.paging.PagingData
import com.example.model.Board
import com.example.model.Login
import com.example.model.LongToken
import com.example.model.ShortToken
import kotlinx.coroutines.flow.Flow

interface InstagramRepository {

    fun fetchShortToken(login: Login): Flow<ShortToken>

    fun fetchLongToken(grantType: String, clientSecret: String, accessToken: String): Flow<LongToken>

    fun fetchBoardInformation(token: String): Flow<PagingData<Board.Item>>

}

