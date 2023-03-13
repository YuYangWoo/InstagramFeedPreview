package com.example.instagramfeedpreview.ui.component.main

import android.graphics.Color
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.instagramfeedpreview.R
import com.example.instagramfeedpreview.databinding.FragmentMainBinding
import com.example.instagramfeedpreview.ui.component.instagram.InstagramViewModel
import com.example.instagramfeedpreview.ui.component.main.adapter.FeedAdapter
import com.example.instagramfeedpreview.ui.component.main.adapter.GridDividerItemDecoration
import com.example.library.binding.BindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : BindingFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val instagramViewModel by activityViewModels<InstagramViewModel>()

    @Inject
    lateinit var feedAdapter: FeedAdapter
    override fun init() {
        super.init()
        initClickListener()
        initObserver()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        with (binding.feedRecyclerView) {
            adapter = feedAdapter
            layoutManager = GridLayoutManager(context, 3)
            addItemDecoration(GridDividerItemDecoration(4, Color.parseColor("#000000")))
        }
    }

    private fun initClickListener() {
        binding.loginButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToInstagramFragment())
        }
    }

    private fun initObserver() {
        lifecycleScope.launchWhenCreated {
            instagramViewModel.boardDTO.collectLatest { boardDTO ->
                binding.loginButton.isVisible = false
                binding.feedRecyclerView.isVisible = true
                feedAdapter.submitList(boardDTO.data)
            }
        }
    }
    companion object {
        private const val TAG = "MainFragment"
    }
}
