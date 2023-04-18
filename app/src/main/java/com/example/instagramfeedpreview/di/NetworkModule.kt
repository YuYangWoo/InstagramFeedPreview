package com.example.instagramfeedpreview.di

import com.example.instagramfeedpreview.data.api.GraphInstagramApiService
import com.example.instagramfeedpreview.data.api.InstagramApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    companion object {
        private const val instagramBaseUrl = "https://api.instagram.com"
        private const val graphInstagramBaseUrl = "https://graph.instagram.com"
    }

    @Provides
    fun provideApiService(): InstagramApiService =
        Retrofit.Builder().baseUrl(instagramBaseUrl).addConverterFactory(GsonConverterFactory.create()).build().create(InstagramApiService::class.java)

    @Provides
    fun provideGraphApiService(): GraphInstagramApiService {
        return Retrofit.Builder().baseUrl(graphInstagramBaseUrl).addConverterFactory(GsonConverterFactory.create()).build().create(GraphInstagramApiService::class.java)
    }
}
