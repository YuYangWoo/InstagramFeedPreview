package com.example.usecase

import com.example.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveUserAccessTokenUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(accessToken: String) {
        userRepository.saveUserAccessToken(accessToken)
    }
}