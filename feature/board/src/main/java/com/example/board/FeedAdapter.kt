package com.example.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.board.databinding.HolderFeedItemBinding
import com.example.network.model.response.BoardInformation
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class FeedAdapter @Inject constructor(): ListAdapter<BoardInformation, FeedAdapter.FeedHolder>(
    DiffFeed
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder =
        FeedHolder(HolderFeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: FeedHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FeedHolder(private val binding: HolderFeedItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(boardInformation: BoardInformation) {
            Glide.with(binding.feedImageview).load(boardInformation.mediaUrl).error(R.drawable.no_image).placeholder(R.drawable.no_image).diskCacheStrategy(
                DiskCacheStrategy.ALL).into(binding.feedImageview)
        }
    }

    object DiffFeed : DiffUtil.ItemCallback<BoardInformation>() {
        override fun areItemsTheSame(oldItem: BoardInformation, newItem: BoardInformation): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: BoardInformation, newItem: BoardInformation): Boolean {
            return oldItem == newItem
        }

    }
}
