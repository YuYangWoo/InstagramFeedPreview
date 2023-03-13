package com.example.instagramfeedpreview.di

import com.example.instagramfeedpreview.data.api.GraphInstagramApiService
import com.example.instagramfeedpreview.data.api.InstagramApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    companion object {
        private const val instagramBaseUrl = "https://api.instagram.com"
        private const val graphInstagramBaseUrl = "https://graph.instagram.com"
    }

    val clientBuilder = OkHttpClient.Builder()
    val loggingInterceptor = HttpLoggingInterceptor()
    @Provides
    fun provideApiService(): InstagramApiService =
        Retrofit.Builder().baseUrl(instagramBaseUrl).addConverterFactory(GsonConverterFactory.create()).build().create(InstagramApiService::class.java)

    @Provides
    fun provideGraphApiService(): GraphInstagramApiService {
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)
        return Retrofit.Builder().baseUrl(graphInstagramBaseUrl).addConverterFactory(GsonConverterFactory.create()).client(clientBuilder.build()).build().create(GraphInstagramApiService::class.java)
    }
}
