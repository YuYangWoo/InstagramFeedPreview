package com.example.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.room.entity.BoardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BoardDao {

    @Query("Select * From BoardEntity")
    fun findBoardItems(): Flow<BoardEntity>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBoard(boardEntity: BoardEntity)
}