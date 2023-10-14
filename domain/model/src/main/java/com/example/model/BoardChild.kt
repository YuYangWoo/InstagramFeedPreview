package com.example.model

data class BoardChild(
    val data: ArrayList<Item>
) {
    data class Item(
        val id: String,
        val media_url: String
    )
}
