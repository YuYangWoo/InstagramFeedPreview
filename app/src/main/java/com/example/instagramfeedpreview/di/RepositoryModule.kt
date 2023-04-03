package com.example.instagramfeedpreview.di

import com.example.instagramfeedpreview.data.repository.remote.InstagramRepositoryImpl
import com.example.instagramfeedpreview.data.repository.local.UserRepositoryImpl
import com.example.instagramfeedpreview.domain.repository.InstagramRepository
import com.example.instagramfeedpreview.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun provideInstagramRepository(instagramRepositoryImpl: InstagramRepositoryImpl): InstagramRepository

    @Binds
    @Singleton
    fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}
