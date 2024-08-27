package com.example.datasource

import androidx.paging.PagingSource
import com.example.model.Board

interface BoardPagingDataSource {
    fun getPagingData(token: String): PagingSource<String, Board.Item>
}