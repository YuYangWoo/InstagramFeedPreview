package com.example.usecase

import com.example.model.Board
import com.example.repository.BoardLocalRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateBoardUseCase @Inject constructor(
    private val boardLocalRepository: BoardLocalRepository
) {
    suspend operator fun invoke(board: Board) {
        boardLocalRepository.updateBoardItems(board)
    }
}