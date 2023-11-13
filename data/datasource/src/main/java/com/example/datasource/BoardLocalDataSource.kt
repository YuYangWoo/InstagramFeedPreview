package com.example.datasource

import com.example.model.Board

interface BoardLocalDataSource {

    suspend fun insert(board: Board)

    suspend fun select(): ArrayList<Board.Item>?

    suspend fun update(board: Board)
}