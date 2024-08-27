package com.example.network.service

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.datasource.BoardLocalDataSource
import com.example.datasource.BoardPagingDataSource
import com.example.datasource.GraphInstagramApiServiceSource
import com.example.model.Board
import com.example.models.response.toDomain
import com.example.models.response.toLocalBoard
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardPagingSourceImpl @Inject constructor(
    private val graphInstagramApiServiceSource: GraphInstagramApiServiceSource,
    private val boardLocalDataSource: BoardLocalDataSource,
) : BoardPagingDataSource {

    override fun getPagingData(token: String): PagingSource<String, Board.Item> {
        return object : PagingSource<String, Board.Item>() {
            override fun getRefreshKey(state: PagingState<String, Board.Item>): String? {
                return null
            }

            override suspend fun load(params: LoadParams<String>): LoadResult<String, Board.Item> {
                val page = params.key

                return try {
                    val networkBoard = token.let { graphInstagramApiServiceSource.getBoardInformation(it, page) }
                    val domainBoard = networkBoard.toDomain()
                    boardLocalDataSource.insert(networkBoard.toLocalBoard())

                    LoadResult.Page(
                        data = domainBoard.items,
                        prevKey = null,
                        nextKey =  networkBoard.paging?.cursors?.after
                    )
                } catch (e: Exception) {
                    return LoadResult.Error(e)
                }
            }

        }
    }
}