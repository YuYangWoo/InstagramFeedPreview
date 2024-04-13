package com.example.models.response

import com.example.model.BoardDetail

data class BoardDetailResponse(
    val data: ArrayList<BoardDetail.Item>
)
fun BoardDetailResponse.toDomain(): BoardDetail = BoardDetail(data)
