package com.example.model

data class Board(
    val boardInformations: ArrayList<BoardInformation>
)

data class BoardInformation(
    val id: String,
    val caption: String,
    val media_url: String
)