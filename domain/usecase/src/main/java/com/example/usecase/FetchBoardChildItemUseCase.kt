package com.example.usecase

import com.example.model.BoardChild
import com.example.repository.BoardRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchBoardChildItemUseCase @Inject constructor(
    private val boardRepository: BoardRepository
) {
    suspend operator fun invoke(mediaId: String, accessToken: String): BoardChild? {
        return boardRepository.fetchBoardChildItems(mediaId, accessToken).firstOrNull()
    }
}