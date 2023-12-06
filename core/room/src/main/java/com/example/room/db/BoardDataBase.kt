package com.example.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.room.dao.BoardDao
import com.example.room.entity.BoardEntity

@Database(entities = [BoardEntity::class], version = 1, exportSchema = false)
abstract class BoardDataBase : RoomDatabase() {
    abstract fun boardDao() : BoardDao
}