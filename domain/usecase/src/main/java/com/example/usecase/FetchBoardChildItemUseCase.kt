package com.example.usecase

import com.example.model.BoardDetail
import com.example.repository.BoardRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
public class FetchBoardChildItemUseCase @Inject constructor(
    private val boardRepository: BoardRepository
) {
    public suspend operator fun invoke(mediaId: String, accessToken: String): BoardDetail? {
        return boardRepository.fetchBoardChildItems(mediaId, accessToken).firstOrNull()
    }
}