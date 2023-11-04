package com.example.model

import com.google.gson.annotations.SerializedName

public data class BoardDetail(
    val items: ArrayList<Item>
) {
    public data class Item(
        val id: String,
        @SerializedName("media_url")
        val mediaUrl: String
    )
}
