package com.example.instagramfeedpreview.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUserAccessToken(accessToken: String)
    fun getUserAccessToken(): Flow<String?>
}
