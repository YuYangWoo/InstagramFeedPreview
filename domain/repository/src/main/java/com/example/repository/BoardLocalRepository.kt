package com.example.repository

import com.example.model.Board

interface BoardLocalRepository {
    suspend fun insertBoardItems(board: Board)

    suspend fun findBoardItems(): ArrayList<Board.Item>?
}