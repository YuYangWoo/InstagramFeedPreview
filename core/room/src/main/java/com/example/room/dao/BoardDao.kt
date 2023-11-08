package com.example.room.dao

import androidx.room.Dao
import com.example.room.entity.Board

@Dao
interface BoardDao {
    fun getBoardList(): List<Board>
    fun insertBoard()
    fun deleteBoard()
    fun updateBoard()
}