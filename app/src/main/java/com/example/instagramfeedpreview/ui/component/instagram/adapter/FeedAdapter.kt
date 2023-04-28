package com.example.instagramfeedpreview.ui.component.instagram.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramfeedpreview.data.model.response.BoardInformation
import com.example.instagramfeedpreview.databinding.HolderFeedItemBinding
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class FeedAdapter @Inject constructor(): ListAdapter<BoardInformation, FeedAdapter.FeedHolder>(DiffFeed) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder =
        FeedHolder(HolderFeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: FeedHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FeedHolder(private val binding: HolderFeedItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(boardInformation: BoardInformation) {
            binding.boardInformation = boardInformation
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