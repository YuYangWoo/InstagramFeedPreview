package com.example.usecase

import com.example.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchUserAccessTokenUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke() : Flow<String> {
        return userRepository.getUserAccessToken()
    }
}