package com.example.usecase

import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchInstagramTokenUseCase @Inject constructor(
    private val instagramRepository: com.example.repository.InstagramRepository
) {
    suspend fun invoke(loginDTO: com.example.network.model.request.LoginDTO): com.example.network.model.response.TokenDTO? {
        return instagramRepository.fetchToken(loginDTO).firstOrNull()
    }
}
