package com.example.instagramfeedpreview.di

import com.example.datasource.UserDataStoreSource
import com.example.datastore.UserDataStoreSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DatastoreModule {
    @Binds
    @Singleton
    fun provideDataStoreModule(userDataStoreSourceImpl: UserDataStoreSourceImpl): UserDataStoreSource
}