package com.example.instagramfeedpreview.domain.repository

import com.example.instagramfeedpreview.data.model.response.TokenDTO
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUserAccessToken(accessToken: String)
    fun getUserAccessToken(): Flow<String?>
}
