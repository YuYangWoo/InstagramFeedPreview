package com.example.repository

import com.example.model.Board

interface BoardLocalRepository {
    suspend fun insertBoardItems(board: Board)

    suspend fun findBoardItems(): List<Board.Item>?

    suspend fun updateBoardItems(board: Board)
}