package com.example.board.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.model.Board

object DiffBoard : DiffUtil.ItemCallback<Board.Item>() {
    override fun areItemsTheSame(oldItem: Board.Item, newItem: Board.Item): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Board.Item, newItem: Board.Item): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

}