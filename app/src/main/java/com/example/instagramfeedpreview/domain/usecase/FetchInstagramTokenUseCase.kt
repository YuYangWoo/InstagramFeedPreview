package com.example.instagramfeedpreview.domain.usecase

import com.example.instagramfeedpreview.data.model.request.LoginDTO
import com.example.instagramfeedpreview.data.model.response.TokenDTO
import com.example.instagramfeedpreview.domain.repository.InstagramRepository
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
