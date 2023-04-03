package com.example.instagramfeedpreview.domain.usecase

import com.example.instagramfeedpreview.data.model.response.BoardDTO
import com.example.instagramfeedpreview.domain.repository.InstagramRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchInstagramBoardUseCase @Inject constructor(private val instagramRepository: InstagramRepository) {
    suspend operator fun invoke(accessToken: String): BoardDTO? {
         return instagramRepository.fetchBoardInformation(accessToken).firstOrNull()
    }
}
