package com.example.dto

import com.example.model.Board
import com.google.gson.annotations.SerializedName

data class BoardDTO(
    @SerializedName("data")
    val items: ArrayList<Board.Item>
)
fun BoardDTO.toDomain(): Board {
    return Board(items)
}
