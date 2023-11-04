package com.example.repository

import com.example.model.BoardDetail
import kotlinx.coroutines.flow.Flow

public interface BoardRepository {
    public fun fetchBoardChildItems(accessToken: String, mediaId: String): Flow<BoardDetail>
}