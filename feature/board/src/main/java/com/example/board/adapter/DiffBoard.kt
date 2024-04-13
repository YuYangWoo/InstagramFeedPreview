package com.example.board.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.model.BoardEntity

object DiffBoard : DiffUtil.ItemCallback<BoardEntity.Item>() {
    override fun areItemsTheSame(oldItem: BoardEntity.Item, newItem: BoardEntity.Item): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BoardEntity.Item, newItem: BoardEntity.Item): Boolean {
        return oldItem == newItem
    }

}