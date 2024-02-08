package com.example.usecase

import com.example.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ManageUserInformationUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun save(accessToken: String) {
        userRepository.saveUserAccessToken(accessToken)
    }
    fun get(): Flow<String?> {
        return userRepository.getUserAccessToken()
    }
}
