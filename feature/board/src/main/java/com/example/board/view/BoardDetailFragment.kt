package com.example.board.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.ViewPager2
import com.example.board.R
import com.example.board.adapter.BoardDetailAdapter
import com.example.board.databinding.FragmentBoardDetailBinding
import com.example.board.state.BoardDetailUiState
import com.example.board.viewmodel.BoardDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BoardDetailFragment : Fragment(R.layout.fragment_board_detail) {
    private val boardDetailViewModel: BoardDetailViewModel by viewModels()
    @Inject
    lateinit var boardDetailAdapter: BoardDetailAdapter
    private var _binding: FragmentBoardDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBoardDetailBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        collect()
    }

    private fun init() {
        binding.viewPager.adapter = boardDetailAdapter
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val totalPageCount = boardDetailAdapter.itemCount
                val currentPage = position + 1

                binding.pageTextView.text = "$currentPage/$totalPageCount"
            }
        })
    }

    private fun collect() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                boardDetailViewModel.boardDetailUiState.collectLatest { boardDetailUiState->
                    when (boardDetailUiState) {
                        is BoardDetailUiState.Error -> {
                            binding.progressBar.isVisible = false
                        }
                        BoardDetailUiState.Loading -> {
                            binding.progressBar.isVisible = true
                        }
                        is BoardDetailUiState.Success -> {
                            binding.progressBar.isVisible = false

                            boardDetailAdapter.submitList(boardDetailUiState.items)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "BoardDetailFragment"
    }
}