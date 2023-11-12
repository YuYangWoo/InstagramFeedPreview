package com.example.room

import com.example.datasource.BoardLocalDataSource
import com.example.model.Board
import com.example.room.dao.BoardDao
import com.example.room.entity.BoardEntity
import com.example.room.entity.toDomain
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardLocalDataSourceImpl @Inject constructor(
    private val boardDao: BoardDao
) : BoardLocalDataSource {

    override suspend fun insert(board: Board) {
        boardDao.insertBoard(BoardEntity(BoardItemWrapper(board.items)))
    }

    override suspend fun select(): ArrayList<Board.Item>? = boardDao.findBoardItems().firstOrNull()?.toDomain()

}