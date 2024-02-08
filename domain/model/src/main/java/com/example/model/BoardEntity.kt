package com.example.model

import com.google.gson.annotations.SerializedName

data class BoardEntity(
    val items: List<Item>,
    val paging: Paging?
) {
    data class Item(
        val id: String,
        val caption: String?,
        @SerializedName("media_url")
        val mediaUrl: String?,
        var order: Int = 0
    )

    data class Paging(
        val cursors: Cursors?,
        val next: String?
    ) {
        data class Cursors(
            val before: String?,
            val after: String?
        )
    }
}

