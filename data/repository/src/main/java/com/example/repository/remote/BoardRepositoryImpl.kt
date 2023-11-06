package com.example.repository.remote

import com.example.datasource.GraphInstagramApiServiceSource
import com.example.dto.toDomain
import com.example.model.BoardDetail
import com.example.repository.BoardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BoardRepositoryImpl @Inject constructor(
    private val graphInstagramApiServiceSource: GraphInstagramApiServiceSource
) : BoardRepository {
    override fun fetchBoardChildItems(mediaId: String, accessToken: String): Flow<BoardDetail> = flow {
        emit(graphInstagramApiServiceSource.getBoardChildInformation(mediaId, accessToken).toDomain())
    }.flowOn(Dispatchers.IO)
}