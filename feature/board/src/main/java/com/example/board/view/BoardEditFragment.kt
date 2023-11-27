package com.example.board.view

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.board.GridDividerItemDecoration
import com.example.board.adapter.BoardEditAdapter
import com.example.board.databinding.FragmentBoardEditBinding
import com.example.board.viewmodel.BoardUiState
import com.example.board.viewmodel.BoardViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BoardEditFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBoardEditBinding? = null
    private val binding: FragmentBoardEditBinding
        get() = _binding!!

    @Inject
    lateinit var boardEditAdapter: BoardEditAdapter

    private val boardViewModel: BoardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBoardEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initObserver()
        initRequest()
    }

    private fun initRequest() {
        boardViewModel.requestBoardLocalItem()
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                boardViewModel.boardUiState.collectLatest { state ->
                    when (state) {
                        is BoardUiState.Success -> {
                            binding.progressBar.isVisible = false
                            boardEditAdapter.submitList(state.data)
                        }
                        is BoardUiState.Loading -> {
                            binding.progressBar.isVisible = true
                        }
                        is BoardUiState.Error -> {
                            binding.progressBar.isVisible = false
                        }
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            adapter = boardEditAdapter
            layoutManager = GridLayoutManager(context, 3)
            addItemDecoration(GridDividerItemDecoration(4, Color.parseColor("#000000")))

        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), theme).apply {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.isDraggable = false
        }

        return bottomSheetDialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}