package com.example.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUserAccessToken(accessToken: String)
    fun getUserAccessToken(): Flow<String?>

}
