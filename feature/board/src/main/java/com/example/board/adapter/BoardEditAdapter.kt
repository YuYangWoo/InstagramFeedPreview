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
class BoardEditAdapter @Inject constructor(): ListAdapter<Board.Item, BoardEditAdapter.BoardHolder>(DiffFeed) {

    private var onItemClick: ((Board.Item, Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardEditAdapter.BoardHolder {
        val binding = HolderFeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BoardHolder(binding)
    }

    override fun onBindViewHolder(holder: BoardEditAdapter.BoardHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    fun setOnItemClickListener(listener: (Board.Item, Int) -> Unit) {
        onItemClick = listener
    }

    class BoardHolder(private val binding: HolderFeedItemBinding) : RecyclerView.ViewHolder(binding.root) {

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

    object DiffFeed : DiffUtil.ItemCallback<Board.Item>() {
        override fun areItemsTheSame(oldItem: Board.Item, newItem: Board.Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Board.Item, newItem: Board.Item): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }

    fun onItemMove(fromPosition: Int, toPosition: Int): List<Board.Item> {
        val items = arrayListOf<Board.Item>()

        notifyItemMoved(toPosition, fromPosition)

        val beforeItem = getItem(fromPosition)
        val afterItem = getItem(toPosition)

        if (beforeItem != null && afterItem != null) {
            items.add(beforeItem)
            items.add(afterItem)

            beforeItem.swapOrderWith(afterItem)
        }
        return items
    }

    private fun Board.Item.swapOrderWith(target: Board.Item) {
        val tmpOrder = this.order
        this.order = target.order
        target.order = tmpOrder
    }

}