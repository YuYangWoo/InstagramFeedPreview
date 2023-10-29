package com.example.board.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.board.R
import com.example.board.databinding.HolderFeedItemBinding
import com.example.model.Board
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class BoardAdapter @Inject constructor(): ListAdapter<Board.Item, BoardAdapter.FeedHolder>(
    DiffFeed
) {

    private var onItemClick: ((Board.Item) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder =
        FeedHolder(HolderFeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: FeedHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    fun setOnItemClickListener(listener: (Board.Item) -> Unit) {
        onItemClick = listener
    }

    class FeedHolder(private val binding: HolderFeedItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(boardInformation: Board.Item, onItemClick: ((Board.Item) -> Unit)?) {
            Glide.with(binding.feedImageview).load(boardInformation.mediaUrl).error(R.drawable.no_image).placeholder(
                R.drawable.no_image
            ).diskCacheStrategy(
                DiskCacheStrategy.ALL).into(binding.feedImageview)

            binding.root.setOnClickListener {
                onItemClick?.let { it -> it(boardInformation) }
            }
        }
    }

    object DiffFeed : DiffUtil.ItemCallback<Board.Item>() {
        override fun areItemsTheSame(oldItem: Board.Item, newItem: Board.Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Board.Item, newItem: Board.Item): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }
}