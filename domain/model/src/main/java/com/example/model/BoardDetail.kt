package com.example.model

import com.google.gson.annotations.SerializedName

data class BoardDetail(
    val items: ArrayList<Item>
) {
    data class Item(
        val id: String,
        @SerializedName("media_url")
        val mediaUrl: String
    )
}
