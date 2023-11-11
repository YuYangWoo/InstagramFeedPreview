package com.example.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.room.BoardItemWrapper
import com.example.room.ListConverter

@Entity
data class BoardEntity(
    @TypeConverters(ListConverter::class) val boards: BoardItemWrapper
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
