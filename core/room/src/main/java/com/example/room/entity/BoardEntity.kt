package com.example.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.Board

@Entity
data class BoardEntity(
    @PrimaryKey
    val id: String,
    val caption: String? = "",
    val mediaUrl: String,
    val order: Int = 0
)

fun List<BoardEntity>.toDomain(): List<Board.Item> = this.map { Board.Item(it.id, it.caption, it.mediaUrl, it.order) }

