package com.example.board.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.board.R
import com.example.board.databinding.HolderBoardDetailItemBinding
import com.example.model.BoardDetailEntity
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class BoardDetailAdapter @Inject constructor(): ListAdapter<BoardDetailEntity.Item, BoardDetailAdapter.BoardDetailItemHolder>(DiffBoardDetail) {

    class BoardDetailItemHolder(private val binding: HolderBoardDetailItemBinding) : ViewHolder(binding.root) {
        fun bind(item: BoardDetailEntity.Item) {
            Glide.with(binding.root.context).load(item.mediaUrl).error(R.drawable.no_image).diskCacheStrategy(
                DiskCacheStrategy.ALL).into(binding.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardDetailItemHolder {
        return BoardDetailItemHolder(HolderBoardDetailItemBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun onBindViewHolder(holder: BoardDetailItemHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object DiffBoardDetail : DiffUtil.ItemCallback<BoardDetailEntity.Item>() {
        override fun areItemsTheSame(oldItem: BoardDetailEntity.Item, newItem: BoardDetailEntity.Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BoardDetailEntity.Item, newItem: BoardDetailEntity.Item): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }
}