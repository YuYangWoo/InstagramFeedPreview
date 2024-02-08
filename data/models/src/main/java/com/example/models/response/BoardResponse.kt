package com.example.models.response

import com.example.model.BoardEntity
import com.example.model.LocalBoardEntity
import com.google.gson.annotations.SerializedName

data class BoardResponse(
    @SerializedName("data")
    val items: List<BoardEntity.Item>,
    val paging: BoardEntity.Paging?
)

fun BoardResponse.toDomain(): BoardEntity {
    return BoardEntity(items, paging)
}

fun BoardResponse.toLocalBoard(): LocalBoardEntity {
    val list = this.items.map { item ->
        LocalBoardEntity.Item(item.id.toLong(), item.mediaUrl, item.order)
    }
    return LocalBoardEntity(ArrayList(list))
}