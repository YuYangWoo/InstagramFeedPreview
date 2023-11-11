package com.example.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.room.entity.BoardEntity

@Dao
interface BoardDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBoard(boardEntity: BoardEntity)
}