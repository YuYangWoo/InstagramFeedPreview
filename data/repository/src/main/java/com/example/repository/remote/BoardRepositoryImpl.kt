package com.example.repository.remote

import com.example.dto.toDomain
import com.example.model.BoardDetail
import com.example.network.service.GraphInstagramApiService
import com.example.repository.BoardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

public class BoardRepositoryImpl @Inject constructor(
    private val graphInstagramApiService: GraphInstagramApiService
) : BoardRepository {
    override fun fetchBoardChildItems(mediaId: String, accessToken: String): Flow<BoardDetail> = flow {
        emit(graphInstagramApiService.getBoardChildInformation(mediaId, accessToken).toDomain())
    }.flowOn(Dispatchers.IO)
}