package com.example.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.room.dao.BoardDao
import com.example.room.entity.Board

@Database(entities = [Board::class], version = 1)
abstract class BoardDataBase : RoomDatabase() {
    abstract fun boardDao() : BoardDao
}