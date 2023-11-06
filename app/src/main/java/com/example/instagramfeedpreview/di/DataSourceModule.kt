package com.example.instagramfeedpreview.di

import com.example.datasource.GraphInstagramApiServiceSource
import com.example.datasource.InstagramLoginDataSource
import com.example.network.service.GraphInstagramApiServiceSourceImpl
import com.example.network.service.InstagramLoginDataSourceImpl
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
}