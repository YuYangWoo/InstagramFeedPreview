package com.example.models.response

import com.example.model.BoardDetail
import com.google.gson.annotations.SerializedName

data class NetworkBoardDetail(
    val data: ArrayList<Item>
) {
    data class Item(
        val id: String,
        @SerializedName("media_url")
        val mediaUrl: String
    )
}

fun NetworkBoardDetail.toDomain(): BoardDetail = BoardDetail(
    items = data.map {
        BoardDetail.Item(it.id, it.mediaUrl)
    } as MutableList<BoardDetail.Item>
)

