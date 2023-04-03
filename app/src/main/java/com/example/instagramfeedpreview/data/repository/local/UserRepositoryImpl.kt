package com.example.instagramfeedpreview.data.repository.local

import com.example.instagramfeedpreview.core.database.UserDataStore
import com.example.instagramfeedpreview.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(private val userDataStore: UserDataStore) : UserRepository {
    override suspend fun saveUserAccessToken(accessToken: String) {
        userDataStore.saveUserAccessToken(accessToken)
    }

    override fun getUserAccessToken(): Flow<String?> {
        return userDataStore.getUserAccessToken()
    }

}
