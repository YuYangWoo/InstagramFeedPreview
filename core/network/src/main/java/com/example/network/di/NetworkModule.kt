package com.example.network.di

import com.example.datasource.GraphInstagramApiServiceSource
import com.example.datasource.InstagramLoginDataSource
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
        val interceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build();

    @Provides
    fun provideApiService(): InstagramLoginDataSource =
        Retrofit.Builder().baseUrl(instagramBaseUrl).addConverterFactory(GsonConverterFactory.create()).build().create(
            InstagramLoginDataSource::class.java)

    @Provides
    fun provideGraphApiService(): GraphInstagramApiServiceSource {
        return Retrofit.Builder().baseUrl(graphInstagramBaseUrl).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(
            GraphInstagramApiServiceSource::class.java)
    }
}
