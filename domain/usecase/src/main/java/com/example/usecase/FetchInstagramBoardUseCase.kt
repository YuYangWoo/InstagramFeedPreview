package com.example.usecase

import androidx.paging.PagingData
import com.example.model.Board
import com.example.repository.InstagramRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchInstagramBoardUseCase @Inject constructor(private val instagramRepository: InstagramRepository) {

    operator fun invoke(): Flow<PagingData<Board.Item>> {
        return instagramRepository.fetchBoardInformation()
    }
}
