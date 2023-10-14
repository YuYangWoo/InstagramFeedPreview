package com.example.dto

import com.example.model.BoardChild

data class BoardChildDTO(
    val data: ArrayList<BoardChild.Item>
)
fun BoardChildDTO.toDomain(): BoardChild = BoardChild(data)
