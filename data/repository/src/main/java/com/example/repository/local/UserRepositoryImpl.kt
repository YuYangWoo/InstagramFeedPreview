package com.example.repository.local

import com.example.datasource.UserDataStoreSource
import com.example.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
public class UserRepositoryImpl @Inject constructor(
    private val userDataStoreSource: UserDataStoreSource) :
    UserRepository {
    override suspend fun saveUserAccessToken(accessToken: String) {
        userDataStoreSource.saveUserAccessToken(accessToken)
    }

    override fun getUserAccessToken(): Flow<String?> {
        return userDataStoreSource.getUserAccessToken()
    }

}
