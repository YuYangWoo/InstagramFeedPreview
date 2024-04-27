package com.example.board.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.board.R
import com.example.board.databinding.HolderFeedItemBinding
import com.example.board.state.BoardUiState
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class BoardAdapter @Inject constructor(): PagingDataAdapter<BoardUiState, BoardAdapter.FeedHolder>(DiffBoard) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder =
        FeedHolder(HolderFeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: FeedHolder, position: Int) {
        getItem(position)?.let(holder::bind)
    }

    class FeedHolder(private val binding: HolderFeedItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            boardUiState: BoardUiState,
        ) {
            binding.root.tag = false
            Glide.with(binding.feedImageview).load(boardUiState.mediaUrl).error(R.drawable.no_image).placeholder(
                R.drawable.no_image
            ).diskCacheStrategy(
                DiskCacheStrategy.ALL).into(binding.feedImageview)

            binding.root.setOnClickListener {
                boardUiState.onClick()
            }
        }
    }

    object DiffBoard : DiffUtil.ItemCallback<BoardUiState>() {
        override fun areItemsTheSame(oldItem: BoardUiState, newItem: BoardUiState): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BoardUiState, newItem: BoardUiState): Boolean {
            return oldItem == newItem
        }

    }
}
