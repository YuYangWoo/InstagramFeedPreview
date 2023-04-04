package com.example.instagramfeedpreview.domain.repository

import com.example.instagramfeedpreview.data.model.request.LoginDTO
import com.example.instagramfeedpreview.data.model.response.BoardDTO
import com.example.instagramfeedpreview.data.model.response.TokenDTO
import kotlinx.coroutines.flow.Flow

interface InstagramRepository {
    fun fetchToken(loginDTO: LoginDTO): Flow<TokenDTO>

    fun fetchBoardInformation(
        accessToken: String
    ): Flow<BoardDTO>
}
