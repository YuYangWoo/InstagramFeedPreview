package com.example.usecase

import com.example.model.Board
import com.example.repository.BoardLocalRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteBoardUseCase @Inject constructor(
    private val boardLocalRepository: BoardLocalRepository
) {
    suspend operator fun invoke(boardItem: Board.Item) {
        boardLocalRepository.deleteBoardItem(boardItem)
    }
}