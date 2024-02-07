package com.example.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.LocalBoardEntity

@Entity
data class BoardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val mediaUrl: String?,
    val order: Int = 0
)

fun List<BoardEntity>.toDomain(): List<LocalBoardEntity.Item> = this.map { LocalBoardEntity.Item(it.id, it.mediaUrl, it.order) }

