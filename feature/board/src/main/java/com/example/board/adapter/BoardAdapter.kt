package com.example.board.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.board.R
import com.example.board.databinding.HolderFeedItemBinding
import com.example.model.Board
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class BoardAdapter @Inject constructor(): PagingDataAdapter<Board.Item, BoardAdapter.FeedHolder>(DiffBoard) {

    private var onItemClick: ((Board.Item, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder =
        FeedHolder(HolderFeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: FeedHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, onItemClick) }
    }

    fun setOnItemClickListener(listener: (Board.Item, Int) -> Unit) {
        onItemClick = listener
    }

    class FeedHolder(private val binding: HolderFeedItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            boardInformation: Board.Item,
            onItemClick: ((Board.Item, Int) -> Unit)?,
        ) {
            binding.root.tag = false
            Glide.with(binding.feedImageview).load(boardInformation.mediaUrl).error(R.drawable.no_image).placeholder(
                R.drawable.no_image
            ).diskCacheStrategy(
                DiskCacheStrategy.ALL).into(binding.feedImageview)

            binding.root.setOnClickListener {
                onItemClick?.let { it -> it(boardInformation, bindingAdapterPosition) }
            }

        }
    }

}
