package com.example.datasource

import com.example.model.LocalBoardEntity
import kotlinx.coroutines.flow.Flow

interface BoardLocalDataSource {

    suspend fun insert(localBoardEntity: LocalBoardEntity)

    suspend fun insertAdditional(localBoardEntity: LocalBoardEntity)

    fun select(): Flow<List<LocalBoardEntity.Item>>

    suspend fun update(localBoardEntity: LocalBoardEntity)

    suspend fun delete(localBoardEntityItem: LocalBoardEntity.Item)
}