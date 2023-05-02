package com.example.instagramfeedpreview.ui.component.instagram.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramfeedpreview.databinding.HolderFeedItemBinding
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class FeedAdapter @Inject constructor(): ListAdapter<com.example.network.model.response.BoardInformation, FeedAdapter.FeedHolder>(DiffFeed) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder =
        FeedHolder(HolderFeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: FeedHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FeedHolder(private val binding: HolderFeedItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(boardInformation: com.example.network.model.response.BoardInformation) {
            binding.boardInformation = boardInformation
        }
    }

    object DiffFeed : DiffUtil.ItemCallback<com.example.network.model.response.BoardInformation>() {
        override fun areItemsTheSame(oldItem: com.example.network.model.response.BoardInformation, newItem: com.example.network.model.response.BoardInformation): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: com.example.network.model.response.BoardInformation, newItem: com.example.network.model.response.BoardInformation): Boolean {
            return oldItem == newItem
        }

    }
}
