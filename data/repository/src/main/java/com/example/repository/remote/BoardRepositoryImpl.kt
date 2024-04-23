package com.example.repository.remote

import com.example.datasource.GraphInstagramApiServiceSource
import com.example.models.response.toDomain
import com.example.model.BoardDetail
import com.example.repository.BoardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BoardRepositoryImpl @Inject constructor(
    private val graphInstagramApiServiceSource: GraphInstagramApiServiceSource
) : BoardRepository {
    override fun fetchBoardDetailItems(id: String, accessToken: String): Flow<BoardDetail> = flow {
        emit(graphInstagramApiServiceSource.getBoardDetailInformation(id, accessToken).toDomain())
    }
}