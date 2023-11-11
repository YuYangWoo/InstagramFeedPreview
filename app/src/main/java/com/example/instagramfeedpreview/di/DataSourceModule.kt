package com.example.instagramfeedpreview.di

import com.example.datasource.BoardLocalDataSource
import com.example.datasource.GraphInstagramApiServiceSource
import com.example.datasource.InstagramLoginDataSource
import com.example.network.service.GraphInstagramApiServiceSourceImpl
import com.example.network.service.InstagramLoginDataSourceImpl
import com.example.room.BoardLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {

    @Binds
    @Singleton
    fun provideInstagramLoginModule(instagramLoginDataSourceImpl: InstagramLoginDataSourceImpl): InstagramLoginDataSource

    @Binds
    @Singleton
    fun provideInstagramGraphModule(graphInstagramApiServiceSourceImpl: GraphInstagramApiServiceSourceImpl): GraphInstagramApiServiceSource

    @Binds
    @Singleton
    fun provideBoardModule(boardLocalDataSourceImpl: BoardLocalDataSourceImpl): BoardLocalDataSource
}