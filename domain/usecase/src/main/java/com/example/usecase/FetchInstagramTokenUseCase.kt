package com.example.usecase

import com.example.model.Login
import com.example.model.Token
import com.example.repository.InstagramRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchInstagramTokenUseCase @Inject constructor(
    private val instagramRepository: InstagramRepository
) {
    suspend fun invoke(login: Login): Token? {
        return instagramRepository.fetchToken(login).firstOrNull()
    }
}
