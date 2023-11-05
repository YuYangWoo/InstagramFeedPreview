package com.example.dto

import com.example.model.BoardDetail

public data class BoardDetailDTO(
    val data: ArrayList<BoardDetail.Item>
)
public fun BoardDetailDTO.toDomain(): BoardDetail = BoardDetail(data)
