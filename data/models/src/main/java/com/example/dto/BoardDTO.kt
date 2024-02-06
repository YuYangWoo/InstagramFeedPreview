package com.example.dto

import com.example.model.Board
import com.example.model.LocalBoard
import com.google.gson.annotations.SerializedName

data class BoardDTO(
    @SerializedName("data")
    val items: List<Board.Item>,
    val paging: Board.Paging?
)

fun BoardDTO.toDomain(): Board {
    return Board(items, paging)
}

fun BoardDTO.toLocalBoard(): LocalBoard {
    val list = this.items.map { item ->
        LocalBoard.Item(item.id.toLong(), item.mediaUrl, item.order)
    }
    return LocalBoard(ArrayList(list))
}