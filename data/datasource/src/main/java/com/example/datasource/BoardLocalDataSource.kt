package com.example.datasource

import androidx.paging.PagingSource
import com.example.model.Board

interface BoardLocalDataSource {

    suspend fun insert(board: Board)

    suspend fun select(): List<Board.Item>?

    suspend fun update(board: Board)

    suspend fun delete(boardItem: Board.Item)

    fun pagingSource(): PagingSource<Int, Board.Item>
}