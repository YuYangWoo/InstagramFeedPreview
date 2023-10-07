package com.example.usecase

import com.example.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class ManageUserInformationUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun save(accessToken: String) {
        userRepository.saveUserAccessToken(accessToken)
    }
    suspend fun get(): String? {
        return userRepository.getUserAccessToken().firstOrNull()
    }
}
