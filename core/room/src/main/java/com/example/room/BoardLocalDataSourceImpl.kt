package com.example.room

import com.example.datasource.BoardLocalDataSource
import com.example.model.LocalBoard
import com.example.room.dao.BoardDao
import com.example.room.entity.BoardEntity
import com.example.room.entity.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Integer.max
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardLocalDataSourceImpl @Inject constructor(
    private val boardDao: BoardDao
) : BoardLocalDataSource {

    override suspend fun insert(localBoard: LocalBoard) {
        localBoard.items.forEach { item ->
            val maxOrder = boardDao.getMaxOrder()
            boardDao.insertBoard(BoardEntity(item.id, item.mediaUrl, max(item.order, maxOrder) + 1))
        }
    }

    override fun select(): Flow<List<LocalBoard.Item>> = boardDao.findBoardItems().map {
            it.toDomain()
        }

    override suspend fun update(localBoard: LocalBoard) {
        localBoard.items.forEach { item ->
            boardDao.updateBoard(BoardEntity(item.id, item.mediaUrl, item.order))
        }
    }

    override suspend fun delete(localBoardItem: LocalBoard.Item) {
        boardDao.deleteBoard(BoardEntity(localBoardItem.id, localBoardItem.mediaUrl, localBoardItem.order))
    }

}