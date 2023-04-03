package com.example.instagramfeedpreview.domain.usecase

import com.example.instagramfeedpreview.data.model.response.TokenDTO
import com.example.instagramfeedpreview.domain.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class HandleUserInformationUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend fun save(accessToken: String) {
        userRepository.saveUserAccessToken(accessToken)
    }
    suspend fun get(): String? {
        return userRepository.getUserAccessToken().firstOrNull()
    }
}
