package com.example.room.di

import com.example.room.db.BoardDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DaoModule {

    @Singleton
    @Provides
    fun provideBoardDao(boardDataBase: BoardDataBase) = boardDataBase.boardDao()
}