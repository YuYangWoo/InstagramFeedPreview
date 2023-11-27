package com.example.room

import androidx.paging.PagingSource
import com.example.datasource.BoardLocalDataSource
import com.example.model.Board
import com.example.room.dao.BoardDao
import com.example.room.entity.BoardEntity
import com.example.room.entity.toDomain
import kotlinx.coroutines.flow.firstOrNull
import java.lang.Integer.max
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardLocalDataSourceImpl @Inject constructor(
    private val boardDao: BoardDao
) : BoardLocalDataSource {

    override suspend fun insert(board: Board) {
        board.items.forEach { item ->
            val maxOrder = boardDao.getMaxOrder()
            boardDao.insertBoard(BoardEntity(item.id, item.caption, item.mediaUrl, max(item.order, maxOrder) + 1))
        }
    }

    override suspend fun select(): List<Board.Item>? = boardDao.findBoardItems().firstOrNull()?.toDomain()

    override suspend fun update(board: Board) {
        board.items.forEach { item ->
            boardDao.updateBoard(BoardEntity(item.id, item.caption, item.mediaUrl, item.order))
        }
    }

    override suspend fun delete(boardItem: Board.Item) {
        boardDao.deleteBoard(BoardEntity(boardItem.id, boardItem.caption, boardItem.mediaUrl, boardItem.order))
    }

    override fun pagingSource(): PagingSource<Int, Board.Item> {
        return boardDao.pagingSource()
    }

}