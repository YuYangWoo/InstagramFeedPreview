package com.example.datasource

import com.example.model.LocalBoard
import kotlinx.coroutines.flow.Flow

interface BoardLocalDataSource {

    suspend fun insert(localBoard: LocalBoard)

    fun select(): Flow<List<LocalBoard.Item>>

    suspend fun update(localBoard: LocalBoard)

    suspend fun delete(localBoardItem: LocalBoard.Item)
}