package com.example.datasource

import kotlinx.coroutines.flow.Flow

interface UserDataStoreSource {

    suspend fun saveUserAccessToken(accessToken: String)

    fun getUserAccessToken(): Flow<String?>
}