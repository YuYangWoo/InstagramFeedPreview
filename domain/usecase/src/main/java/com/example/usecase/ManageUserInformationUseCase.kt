package com.example.usecase

import com.example.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

public class ManageUserInformationUseCase @Inject constructor(private val userRepository: UserRepository) {
    public suspend fun save(accessToken: String) {
        userRepository.saveUserAccessToken(accessToken)
    }
    public suspend fun get(): String? {
        return userRepository.getUserAccessToken().firstOrNull()
    }
}
