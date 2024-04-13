package com.example.repository

import com.example.model.LocalBoardEntity
import kotlinx.coroutines.flow.Flow

interface BoardLocalRepository {
    suspend fun insertBoardItems(localBoardEntity: LocalBoardEntity)

    suspend fun insertBoardItem(localBoardEntity: LocalBoardEntity)

    fun findBoardItems(): Flow<List<LocalBoardEntity.Item>>

    suspend fun updateBoardItems(localBoardEntity: LocalBoardEntity)

    suspend fun deleteBoardItem(localBoardEntityItem: LocalBoardEntity.Item)
}