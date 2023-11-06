package com.example.dto

import com.example.model.BoardDetail

data class BoardDetailDTO(
    val data: ArrayList<BoardDetail.Item>
)
fun BoardDetailDTO.toDomain(): BoardDetail = BoardDetail(data)
