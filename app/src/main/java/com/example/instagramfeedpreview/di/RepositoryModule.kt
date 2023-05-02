package com.example.instagramfeedpreview.di

import com.example.repository.InstagramRepository
import com.example.repository.UserRepository
import com.example.repository.local.UserRepositoryImpl
import com.example.repository.remote.InstagramRepositoryImpl
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
