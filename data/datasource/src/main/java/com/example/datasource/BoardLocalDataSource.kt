package com.example.datasource

import androidx.paging.PagingSource
import com.example.model.Board
import kotlinx.coroutines.flow.Flow

interface BoardLocalDataSource {

    suspend fun insert(board: Board)

    fun select(): Flow<List<Board.Item>>

    suspend fun update(board: Board)

    suspend fun delete(boardItem: Board.Item)

    fun pagingSource(): PagingSource<Int, Board.Item>
}