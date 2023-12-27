package com.example.board.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.board.GridDividerItemDecoration
import com.example.board.ItemMoveCallback
import com.example.board.adapter.BoardEditAdapter
import com.example.board.databinding.FragmentBoardEditBinding
import com.example.board.viewmodel.BoardLocalUiState
import com.example.board.viewmodel.BoardViewModel
import com.example.model.LocalBoard
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

    private var dbTransactionStatus = ""

    private val pickerMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { selectedImageUri ->
            boardViewModel.insertAdditionalBoardItem(LocalBoard(arrayListOf(LocalBoard.Item(0L, selectedImageUri.toString()))))
            dbTransactionStatus = "INSERT"
        }
    }

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

        Toast.makeText(requireContext(), "편집모드", Toast.LENGTH_SHORT).show()

        initRecyclerView()
        initObserver()
        initRequest()
        initClickListener()
    }

    private fun initRequest() {
        boardViewModel.requestBoardLocalItem()
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                boardViewModel.boardLocalUiState.collectLatest { state ->
                    when (state) {
                        is BoardLocalUiState.Success -> {
                            binding.progressBar.isVisible = false

                            boardEditAdapter.submitList(state.data)

                            if (dbTransactionStatus == "INSERT") {
                                binding.recyclerView.layoutManager?.smoothScrollToPosition(binding.recyclerView, null, 0)
                            }
                        }
                        is BoardLocalUiState.Loading -> {
                            binding.progressBar.isVisible = true
                        }
                        is BoardLocalUiState.Error -> {
                            binding.progressBar.isVisible = false
                        }
                    }
                }
            }
        }
    }

    private fun initClickListener() {
        binding.trashCanImageView.setOnClickListener {
            binding.trashCanImageView.tag = if (binding.trashCanImageView.tag == true) {
                binding.trashCanImageView.isSelected = false
                Toast.makeText(requireContext(), "삭제모드 취소", Toast.LENGTH_SHORT).show()

                false
            } else {
                binding.trashCanImageView.isSelected = true
                Toast.makeText(requireContext(), "삭제할 사진을 선택해주세요.", Toast.LENGTH_SHORT).show()

                true
            }
        }

        binding.addPhotoImageView.setOnClickListener {
            pickerMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun initRecyclerView() {
        with (binding.recyclerView) {
            adapter = boardEditAdapter.apply {
                setOnItemClickListener { board, position ->
                    when (binding.trashCanImageView.tag) {
                        true -> {
                            boardViewModel.deleteBoardItem(board)
                            dbTransactionStatus = "DELETE"
                        }
                        else -> {
                           // Nothing
                        }
                    }

                }
            }
            layoutManager = GridLayoutManager(context, 3)

            val callback = ItemMoveCallback(
                boardEditAdapter = boardEditAdapter,
                onCompleteListener = {
                    boardViewModel.updateBoardItem(LocalBoard(it))
                    dbTransactionStatus = "UPDATE"
                })
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(binding.recyclerView)

            addItemDecoration(GridDividerItemDecoration(4, Color.parseColor("#000000")))
        }
    }

    companion object {
        private const val TAG = "BoardEditFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}