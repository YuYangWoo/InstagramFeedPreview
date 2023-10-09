package com.example.dto

import com.example.model.Board
import com.example.model.BoardInformation
import com.google.gson.annotations.SerializedName

data class BoardDTO(
    @SerializedName("data")
    val boardInformations: ArrayList<BoardInformation>
)
fun BoardDTO.toDomain(): Board {
    return Board(boardInformations)
}
