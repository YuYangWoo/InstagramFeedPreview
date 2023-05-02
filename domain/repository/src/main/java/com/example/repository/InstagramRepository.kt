package com.example.repository

import com.example.network.model.request.LoginDTO
import com.example.network.model.response.BoardDTO
import com.example.network.model.response.TokenDTO
import kotlinx.coroutines.flow.Flow

interface InstagramRepository {
    fun fetchToken(loginDTO: LoginDTO): Flow<TokenDTO>

    fun fetchBoardInformation(
        accessToken: String
    ): Flow<BoardDTO>
}
