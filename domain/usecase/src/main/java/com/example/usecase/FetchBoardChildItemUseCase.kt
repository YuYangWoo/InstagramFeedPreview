package com.example.usecase

import com.example.model.BoardDetailEntity
import com.example.repository.BoardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchBoardChildItemUseCase @Inject constructor(
    private val boardRepository: BoardRepository
) {
    operator fun invoke(mediaId: String, accessToken: String): Flow<BoardDetailEntity> {
        return boardRepository.fetchBoardDetailItems(mediaId, accessToken)
    }
}