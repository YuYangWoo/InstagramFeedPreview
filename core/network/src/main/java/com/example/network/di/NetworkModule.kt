package com.example.network.di

import com.example.network.service.GraphInstagramApiService
import com.example.network.service.InstagramLoginService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object {
        private const val instagramBaseUrl = "https://api.instagram.com"
        private const val graphInstagramBaseUrl = "https://graph.instagram.com"
    }
        val interceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build();

    @Provides
    @Singleton
    fun provideApiService(): InstagramLoginService =
        Retrofit.Builder().baseUrl(instagramBaseUrl).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(
            InstagramLoginService::class.java)

    @Provides
    @Singleton
    fun provideGraphApiService(): GraphInstagramApiService {
        return Retrofit.Builder().baseUrl(graphInstagramBaseUrl).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(
            GraphInstagramApiService::class.java)
    }
}
