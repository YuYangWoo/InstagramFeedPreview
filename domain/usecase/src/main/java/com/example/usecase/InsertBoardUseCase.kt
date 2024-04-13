package com.example.usecase

import com.example.model.LocalBoardEntity
import com.example.repository.BoardLocalRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertBoardUseCase @Inject constructor(
    private val boardLocalRepository: BoardLocalRepository
) {

    suspend operator fun invoke(localBoardEntity: LocalBoardEntity) {
        boardLocalRepository.insertBoardItems(localBoardEntity)
    }
    suspend fun invokeAdditional(localBoardEntity: LocalBoardEntity) {
        boardLocalRepository.insertBoardItem(localBoardEntity)
    }
}