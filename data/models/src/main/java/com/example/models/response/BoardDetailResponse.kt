package com.example.models.response

import com.example.model.BoardDetailEntity

data class BoardDetailResponse(
    val data: ArrayList<BoardDetailEntity.Item>
)
fun BoardDetailResponse.toDomain(): BoardDetailEntity = BoardDetailEntity(data)
