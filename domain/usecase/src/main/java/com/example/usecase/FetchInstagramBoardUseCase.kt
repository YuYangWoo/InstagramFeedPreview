package com.example.usecase

import com.example.network.model.response.BoardDTO
import com.example.repository.InstagramRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchInstagramBoardUseCase @Inject constructor(private val instagramRepository: InstagramRepository) {
    suspend operator fun invoke(accessToken: String): BoardDTO? {
         return instagramRepository.fetchBoardInformation(accessToken).firstOrNull()
    }
}
