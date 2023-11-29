package com.example.usecase

import androidx.paging.PagingData
import com.example.model.Board
import com.example.repository.InstagramRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchInstagramBoardUseCase @Inject constructor(private val instagramRepository: InstagramRepository) {

    suspend operator fun invoke(accessToken: String, after: String? = null): PagingData<Board.Item>? {
        return instagramRepository.fetchBoardInformation(accessToken, after).firstOrNull()
    }
}
