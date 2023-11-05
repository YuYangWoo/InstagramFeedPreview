package com.example.network.di

import com.example.network.service.GraphInstagramApiServiceSourceImpl
import com.example.network.service.InstagramLoginDataSourceImpl
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
    fun provideApiService(): InstagramLoginDataSourceImpl =
        Retrofit.Builder().baseUrl(instagramBaseUrl).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(
            InstagramLoginDataSourceImpl::class.java)

    @Provides
    fun provideGraphApiService(): GraphInstagramApiServiceSourceImpl {
        return Retrofit.Builder().baseUrl(graphInstagramBaseUrl).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(
            GraphInstagramApiServiceSourceImpl::class.java)
    }
}
