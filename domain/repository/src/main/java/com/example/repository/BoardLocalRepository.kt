package com.example.repository

import com.example.model.Board
import kotlinx.coroutines.flow.Flow

interface BoardLocalRepository {
    suspend fun insertBoardItems(board: Board)

    fun findBoardItems(): Flow<List<Board.Item>>

    suspend fun updateBoardItems(board: Board)

    suspend fun deleteBoardItem(boardItem: Board.Item)
}