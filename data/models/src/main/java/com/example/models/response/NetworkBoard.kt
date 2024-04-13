package com.example.models.response

import com.example.model.Board
import com.example.model.LocalBoard
import com.google.gson.annotations.SerializedName

data class NetworkBoard(
    @SerializedName("data")
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

fun NetworkBoard.toDomain(): Board {
    val items = items.map {
        Board.Item(it.id, it.caption, it.mediaUrl, it.order)
    }
    val paging = Board.Paging(Board.Paging.Cursors(paging?.cursors?.before, paging?.cursors?.after), paging?.next)
    return Board(items, paging)
}

fun NetworkBoard.toLocalBoard(): LocalBoard {
    val list = this.items.map { item ->
        LocalBoard.Item(item.id.toLong(), item.mediaUrl, item.order)
    }
    return LocalBoard(ArrayList(list))
}