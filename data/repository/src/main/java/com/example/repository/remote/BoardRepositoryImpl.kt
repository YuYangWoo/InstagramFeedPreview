package com.example.repository.remote

import android.util.Log
import com.example.dto.toDomain
import com.example.model.BoardChild
import com.example.network.service.GraphInstagramApiService
import com.example.repository.BoardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BoardRepositoryImpl @Inject constructor(
    private val graphInstagramApiService: GraphInstagramApiService
) : BoardRepository {
    override fun fetchBoardChildItems(mediaId: String, accessToken: String): Flow<BoardChild> = flow {
        emit(graphInstagramApiService.getBoardChildInformation(mediaId, accessToken).toDomain())
    }.flowOn(Dispatchers.IO)
}