package com.example.usecase.di

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
    fun provideInstagramRepository(instagramRepositoryImpl: InstagramRepositoryImpl): com.example.repository.InstagramRepository

    @Binds
    @Singleton
    fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): com.example.repository.UserRepository
}
