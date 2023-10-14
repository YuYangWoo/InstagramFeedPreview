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
    private val boardDetailViewModel: BoardDetailViewModel by activityViewModels()
    @Inject
    lateinit var boardDetailAdapter: BoardDetailAdapter

    override fun init() {
        super.init()
        val mediaId = arguments?.getString("id")
        initObserver()
        binding.viewPager.adapter = boardDetailAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            boardDetailViewModel.requestBoardChildItems(mediaId!!)
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                boardDetailViewModel.uiState.collectLatest { state ->
                    when (state) {
                        is UiState.Success -> {
                            binding.progressBar.isVisible = false
                            boardDetailAdapter.submitList(state.data.data)
                            Log.d(TAG, state.data.toString())
                        }
                        is UiState.Loading -> {
                            binding.progressBar.isVisible = true
                        }
                        is UiState.Error -> {
                            binding.progressBar.isVisible = false
                            Log.d(TAG, state.message)
                        }
                        is UiState.Empty -> {

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