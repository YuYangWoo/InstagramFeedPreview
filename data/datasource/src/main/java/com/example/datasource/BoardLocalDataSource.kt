package com.example.datasource

import com.example.model.Board

interface BoardLocalDataSource {

    suspend fun insert(board: Board)
}