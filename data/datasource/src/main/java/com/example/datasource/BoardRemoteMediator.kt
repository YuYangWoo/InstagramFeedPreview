package com.example.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.model.Board
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Singleton
class BoardRemoteMediator @Inject constructor(
    private val graphInstagramApiServiceSource: GraphInstagramApiServiceSource,
    private val boardLocalDataSource: BoardLocalDataSource,
    private val accessToken: String
) : RemoteMediator<Int, Board.Item>() {

    private var afterKey: String? = null

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Board.Item>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val key = afterKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                    key
                }
            }

            val response = graphInstagramApiServiceSource.getBoardInformation(accessToken, loadKey)
            afterKey = response.paging?.cursors?.after

            boardLocalDataSource.insert(Board(response.items, response.paging))

            MediatorResult.Success(endOfPaginationReached = response.paging?.cursors?.after.isNullOrEmpty())
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}