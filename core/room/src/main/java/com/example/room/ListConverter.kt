package com.example.room

import androidx.room.TypeConverter
import com.example.model.Board
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListConverter {
    @TypeConverter
    fun listToJson(value: BoardItemWrapper?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): BoardItemWrapper? {
        val type = object : TypeToken<BoardItemWrapper>() {}.type
        return Gson().fromJson(value, type)
    }
}

data class BoardItemWrapper(val items: ArrayList<Board.Item>)