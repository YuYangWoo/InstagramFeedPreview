package com.example.dto

import com.example.model.Board
import com.google.gson.annotations.SerializedName

public data class BoardDTO(
    @SerializedName("data")
    val items: ArrayList<Board.Item>
)
public fun BoardDTO.toDomain(): Board {
    return Board(items)
}
