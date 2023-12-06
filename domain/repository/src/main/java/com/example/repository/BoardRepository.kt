package com.example.repository

import com.example.model.BoardDetail
import kotlinx.coroutines.flow.Flow

interface BoardRepository {
    fun fetchBoardDetailItems(accessToken: String, mediaId: String): Flow<BoardDetail>
}