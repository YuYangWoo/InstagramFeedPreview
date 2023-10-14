package com.example.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.board.databinding.HolderFeedItemBinding
import com.example.model.BoardInformation
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class FeedAdapter @Inject constructor(): ListAdapter<BoardInformation, FeedAdapter.FeedHolder>(
    DiffFeed
) {

    private var onItemClick: ((BoardInformation) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder =
        FeedHolder(HolderFeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: FeedHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    fun setOnItemClickListener(listener: (BoardInformation) -> Unit) {
        onItemClick = listener
    }

    class FeedHolder(private val binding: HolderFeedItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(boardInformation: BoardInformation, onItemClick: ((BoardInformation) -> Unit)?) {
            Glide.with(binding.feedImageview).load(boardInformation.media_url).error(R.drawable.no_image).placeholder(R.drawable.no_image).diskCacheStrategy(
                DiskCacheStrategy.ALL).into(binding.feedImageview)

            binding.root.setOnClickListener {
                onItemClick?.let { it -> it(boardInformation) }
            }
        }
    }

    object DiffFeed : DiffUtil.ItemCallback<BoardInformation>() {
        override fun areItemsTheSame(oldItem: BoardInformation, newItem: BoardInformation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BoardInformation, newItem: BoardInformation): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }
}
