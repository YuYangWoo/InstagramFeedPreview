package com.example.room.di

import android.content.Context
import androidx.room.Room
import com.example.room.db.BoardDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideBoardDataBase(context: Context) = Room.databaseBuilder(context.applicationContext, BoardDataBase::class.java, "board_database").build()
}