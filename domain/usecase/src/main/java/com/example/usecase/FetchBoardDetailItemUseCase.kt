package com.example.usecase

import com.example.model.BoardDetail
import com.example.repository.BoardRepository
import com.example.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchBoardDetailItemUseCase @Inject constructor(
    private val boardRepository: BoardRepository,
    private val userRepository: UserRepository,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(id: String): Flow<BoardDetail> {
        return userRepository.getUserAccessToken().flatMapLatest { accessToken ->
            boardRepository.fetchBoardDetailItems(id, accessToken)
        }
    }
}