package com.example.board

import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
        binding.viewPager.adapter = boardDetailAdapter
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