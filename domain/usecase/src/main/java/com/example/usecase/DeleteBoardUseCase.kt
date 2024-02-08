package com.example.usecase

import com.example.model.LocalBoardEntity
import com.example.repository.BoardLocalRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeleteBoardUseCase @Inject constructor(
    private val boardLocalRepository: BoardLocalRepository
) {
    suspend operator fun invoke(localBoardEntityItem: LocalBoardEntity.Item) {
        boardLocalRepository.deleteBoardItem(localBoardEntityItem)
    }
}