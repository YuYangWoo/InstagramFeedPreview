package com.example.models.response

import com.example.model.BoardDetail

data class NetworkBoardDetail(
    val data: ArrayList<BoardDetail.Item>
)
fun NetworkBoardDetail.toDomain(): BoardDetail = BoardDetail(data)
