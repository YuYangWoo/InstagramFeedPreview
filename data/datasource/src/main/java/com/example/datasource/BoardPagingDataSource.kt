package com.example.datasource

import androidx.paging.PagingSource
import com.example.models.response.NetworkBoard

interface BoardPagingDataSource {
    fun getPagingData(token: String): PagingSource<String, NetworkBoard.Item>
}