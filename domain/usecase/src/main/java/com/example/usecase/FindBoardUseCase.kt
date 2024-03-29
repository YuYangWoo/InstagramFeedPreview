package com.example.usecase

import com.example.model.LocalBoardEntity
import com.example.repository.BoardLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FindBoardUseCase @Inject constructor(private val boardLocalRepository: BoardLocalRepository) {

    operator fun invoke(): Flow<List<LocalBoardEntity.Item>> = boardLocalRepository.findBoardItems()

}