package com.example.repository

import com.example.model.BoardChild
import kotlinx.coroutines.flow.Flow

interface BoardRepository {
    fun fetchBoardChildItems(accessToken: String, mediaId: String): Flow<BoardChild>
}