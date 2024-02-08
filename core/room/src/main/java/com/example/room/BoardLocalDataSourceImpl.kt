package com.example.room

import com.example.datasource.BoardLocalDataSource
import com.example.model.LocalBoardEntity
import com.example.room.dao.BoardDao
import com.example.room.entity.BoardEntity
import com.example.room.entity.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.Integer.max
import java.lang.Integer.min
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardLocalDataSourceImpl @Inject constructor(
    private val boardDao: BoardDao
) : BoardLocalDataSource {

    override suspend fun insert(localBoardEntity: LocalBoardEntity) {
        localBoardEntity.items.forEach { item ->
            val maxOrder = boardDao.getMaxOrder()
            boardDao.insertBoard(BoardEntity(item.id, item.mediaUrl, max(item.order, maxOrder) + 1))
        }
    }

    override suspend fun insertAdditional(localBoardEntity: LocalBoardEntity) {
        localBoardEntity.items.forEach { item ->
            val minOrder = boardDao.getMinOrder()
            boardDao.insertBoard(BoardEntity(item.id, item.mediaUrl, min(item.order, minOrder) - 1))
        }
    }

    override fun select(): Flow<List<LocalBoardEntity.Item>> = boardDao.findBoardItems().map {
            it.toDomain()
        }

    override suspend fun update(localBoardEntity: LocalBoardEntity) {
        localBoardEntity.items.forEach { item ->
            boardDao.updateBoard(BoardEntity(item.id, item.mediaUrl, item.order))
        }
    }

    override suspend fun delete(localBoardEntityItem: LocalBoardEntity.Item) {
        boardDao.deleteBoard(BoardEntity(localBoardEntityItem.id, localBoardEntityItem.mediaUrl, localBoardEntityItem.order))
    }

}