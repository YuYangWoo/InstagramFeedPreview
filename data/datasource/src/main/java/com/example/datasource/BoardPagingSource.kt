package com.example.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.dto.toLocalBoard
import com.example.model.Board
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoardPagingSource @Inject constructor(
    private val graphInstagramApiServiceSource: GraphInstagramApiServiceSource,
    private val boardLocalDataSource: BoardLocalDataSource,
    private val userDataStoreSource: UserDataStoreSource
    ) : PagingSource<String, Board.Item>() {

    override fun getRefreshKey(state: PagingState<String, Board.Item>): String? {
        return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Board.Item> {
        val page = params.key

        return try {
            val token = userDataStoreSource.getUserAccessToken().firstOrNull()
            val boardDTO = token?.let { graphInstagramApiServiceSource.getBoardInformation(it, page) }
                ?: return LoadResult.Error(Exception("token is Null"))

            boardLocalDataSource.insert(boardDTO.toLocalBoard())

            LoadResult.Page(
                data = boardDTO.items,
                prevKey = null,
                nextKey =  boardDTO.paging?.cursors?.after
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

}