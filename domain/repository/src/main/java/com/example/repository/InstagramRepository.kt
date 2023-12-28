package com.example.repository

import androidx.paging.PagingData
import com.example.model.Board
import com.example.model.Login
import com.example.model.Token
import kotlinx.coroutines.flow.Flow

interface InstagramRepository {

    fun fetchToken(login: Login): Flow<Token>

    fun fetchBoardInformation(): Flow<PagingData<Board.Item>>

}

