package com.example.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.room.entity.BoardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BoardDao {

    @Query("Select * From BoardEntity ORDER BY `order` ASC")
    fun findBoardItems(): Flow<List<BoardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoard(boardEntity: BoardEntity)

    @Update
    suspend fun updateBoard(boardEntity: BoardEntity)

    @Query("SELECT MAX(`order`) FROM BoardEntity")
    suspend fun getMaxOrder(): Int

    @Query("SELECT MIN(`order`) FROM BoardEntity")
    suspend fun getMinOrder(): Int

    @Delete
    suspend fun deleteBoard(boardEntity: BoardEntity)
}
