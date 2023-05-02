package com.example.usecase

import com.example.network.model.request.LoginDTO
import com.example.network.model.response.TokenDTO
import com.example.repository.InstagramRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchInstagramTokenUseCase @Inject constructor(
    private val instagramRepository: InstagramRepository
) {
    suspend fun invoke(loginDTO: LoginDTO): TokenDTO? {
        return instagramRepository.fetchToken(loginDTO).firstOrNull()
    }
}
