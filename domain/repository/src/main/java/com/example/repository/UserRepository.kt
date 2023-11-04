package com.example.repository

import kotlinx.coroutines.flow.Flow

public interface UserRepository {
    public suspend fun saveUserAccessToken(accessToken: String)
    public fun getUserAccessToken(): Flow<String?>
}
