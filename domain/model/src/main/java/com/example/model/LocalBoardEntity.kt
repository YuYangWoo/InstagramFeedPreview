package com.example.model

data class LocalBoardEntity(
    val items: ArrayList<Item>
) {
    data class Item(
        val id: Long,
        val mediaUrl: String?,
        var order: Int = 0
    )
}