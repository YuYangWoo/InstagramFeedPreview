package com.example.repository.local

import com.example.datasource.BoardLocalDataSource
import com.example.model.LocalBoardEntity
import com.example.repository.BoardLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardLocalRepositoryImpl @Inject constructor(
    private val boardLocalDataSource: BoardLocalDataSource
) : BoardLocalRepository {
    override suspend fun insertBoardItems(localBoardEntity: LocalBoardEntity) {
        boardLocalDataSource.insert(localBoardEntity)
    }

    override suspend fun insertBoardItem(localBoardEntity: LocalBoardEntity) {
        boardLocalDataSource.insertAdditional(localBoardEntity)
    }

    override fun findBoardItems(): Flow<List<LocalBoardEntity.Item>> {
        return boardLocalDataSource.select()
    }

    override suspend fun updateBoardItems(localBoardEntity: LocalBoardEntity) {
        boardLocalDataSource.update(localBoardEntity)
    }

    override suspend fun deleteBoardItem(localBoardEntityItem: LocalBoardEntity.Item) {
        boardLocalDataSource.delete(localBoardEntityItem)
    }

}