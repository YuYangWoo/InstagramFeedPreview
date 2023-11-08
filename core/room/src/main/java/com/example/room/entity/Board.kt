package com.example.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Board(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "media_url") val mediaUrl: String?
)