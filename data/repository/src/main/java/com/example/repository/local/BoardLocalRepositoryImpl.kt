package com.example.repository.local

import com.example.datasource.BoardLocalDataSource
import com.example.model.LocalBoard
import com.example.repository.BoardLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardLocalRepositoryImpl @Inject constructor(
    private val boardLocalDataSource: BoardLocalDataSource
) : BoardLocalRepository {
    override suspend fun insertBoardItems(localBoard: LocalBoard) {
        boardLocalDataSource.insert(localBoard)
    }

    override suspend fun insertBoardItem(localBoard: LocalBoard) {
        boardLocalDataSource.insertAdditional(localBoard)
    }

    override fun findBoardItems(): Flow<List<LocalBoard.Item>> {
        return boardLocalDataSource.select()
    }

    override suspend fun updateBoardItems(localBoard: LocalBoard) {
        boardLocalDataSource.update(localBoard)
    }

    override suspend fun deleteBoardItem(localBoardItem: LocalBoard.Item) {
        boardLocalDataSource.delete(localBoardItem)
    }

}