package com.example.instagramfeedpreview.data.model.response

import com.google.gson.annotations.SerializedName

data class BoardDTO(
    @SerializedName("data")
    val data: ArrayList<BoardInformation>
)

data class BoardInformation(
    val id: String,
    val caption: String,
    @SerializedName("media_url")
    val mediaUrl: String
)
