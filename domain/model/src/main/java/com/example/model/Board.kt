package com.example.model

import com.google.gson.annotations.SerializedName

data class Board(
    val items: List<Item>
) {
    data class Item(
        val id: String,
        val caption: String,
        @SerializedName("media_url")
        val mediaUrl: String,
        var order: Int = 0
    )
}

