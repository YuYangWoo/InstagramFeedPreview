package com.example.instagramfeedpreview.di

import com.example.datasource.BoardLocalDataSource
import com.example.datasource.BoardPagingDataSource
import com.example.datasource.GraphInstagramApiServiceSource
import com.example.datasource.InstagramLoginDataSource
import com.example.network.datasource.BoardPagingSourceImpl
import com.example.network.datasource.GraphInstagramDataSourceImpl
import com.example.network.datasource.InstagramLoginDataSourceImpl
import com.example.network.service.GraphInstagramApiService
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
    fun bindInstagramLoginDataSource(instagramLoginDataSourceImpl: InstagramLoginDataSourceImpl): InstagramLoginDataSource

    @Binds
    @Singleton
    fun bindGraphInstagramApiServiceSource(graphInstagramDataSourceImpl: GraphInstagramDataSourceImpl): GraphInstagramApiServiceSource

    @Binds
    @Singleton
    fun bindBoardLocalDataSource(boardLocalDataSourceImpl: BoardLocalDataSourceImpl): BoardLocalDataSource

    @Binds
    @Singleton
    fun bindBoardPagingDataSource(boardPagingSourceImpl: BoardPagingSourceImpl): BoardPagingDataSource
}