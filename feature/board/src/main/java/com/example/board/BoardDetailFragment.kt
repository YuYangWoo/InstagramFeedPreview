package com.example.board

import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.example.board.adapter.BoardDetailAdapter
import com.example.board.databinding.FragmentBoardDetailBinding
import com.example.library.binding.BindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BoardDetailFragment : BindingFragment<FragmentBoardDetailBinding>(R.layout.fragment_board_detail) {
    private val boardViewModel: BoardViewModel by activityViewModels()
    @Inject
    lateinit var boardDetailAdapter: BoardDetailAdapter

    override fun init() {
        super.init()
        initObserver()
        initAdapter()
    }

    private fun initAdapter() {
        binding.viewPager.adapter = boardDetailAdapter
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val totalPageCount = boardDetailAdapter.itemCount
                val currentPage = position + 1

                binding.pageTextView.text = "$currentPage/$totalPageCount"
            }
        })
    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                boardViewModel.boardDetailUiState.collectLatest { state ->
                    when (state) {
                        is BoardDetailUiState.Success -> {
                            binding.progressBar.isVisible = false
                            boardDetailAdapter.submitList(state.data.items)
                            Log.d(TAG, state.data.toString())
                        }
                        is BoardDetailUiState.Loading -> {
                            binding.progressBar.isVisible = true
                        }
                        is BoardDetailUiState.Error -> {
                            binding.progressBar.isVisible = false
                            Log.d(TAG, state.message)
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "BoardDetailFragment"
    }
}