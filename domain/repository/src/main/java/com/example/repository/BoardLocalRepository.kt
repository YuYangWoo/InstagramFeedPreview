package com.example.repository

import com.example.model.LocalBoard
import kotlinx.coroutines.flow.Flow

interface BoardLocalRepository {
    suspend fun insertBoardItems(localBoard: LocalBoard)

    suspend fun insertBoardItem(localBoard: LocalBoard)

    fun findBoardItems(): Flow<List<LocalBoard.Item>>

    suspend fun updateBoardItems(localBoard: LocalBoard)

    suspend fun deleteBoardItem(localBoardItem: LocalBoard.Item)
}