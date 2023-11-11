package com.example.repository.local

import com.example.datasource.BoardLocalDataSource
import com.example.model.Board
import com.example.repository.BoardLocalRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardLocalRepositoryImpl @Inject constructor(
    private val boardLocalDataSource: BoardLocalDataSource
) : BoardLocalRepository {
    override suspend fun insertBoardItems(board: Board) {
        boardLocalDataSource.insert(board)
    }

}