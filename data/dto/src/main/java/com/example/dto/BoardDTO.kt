package com.example.dto

import com.example.model.Board
import com.google.gson.annotations.SerializedName

data class BoardDTO(
    @SerializedName("data")
    val items: List<Board.Item>,
    val paging: Board.Paging?
)

fun BoardDTO.toDomain(): Board {
    return Board(items, paging)
}
