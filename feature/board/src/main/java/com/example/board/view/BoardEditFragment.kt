package com.example.board.view

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
import com.example.model.Board
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
    private var localBoardItems = ArrayList<Board.Item>()
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { selectedImageUri ->
            localBoardItems.add(Board.Item("1", "dd", selectedImageUri.toString(), localBoardItems.last().order + 1))
            boardViewModel.insertBoardItem(Board(localBoardItems, null))
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
                            localBoardItems = state.data as ArrayList<Board.Item>
                            boardEditAdapter.submitList(state.data)
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
            if (hasStoragePermission()) {
                openGallery()
            } else {
                requestStoragePermission()
            }
        }
    }

    private fun openGallery() {
        getContent.launch("image/*")
    }

    private fun initRecyclerView() {
        with (binding.recyclerView) {
            adapter = boardEditAdapter.apply {
                setOnItemClickListener { board, position ->
                    when (binding.trashCanImageView.tag) {
                        true -> {
                            boardViewModel.deleteBoardItem(board)
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
                    boardViewModel.updateBoardItem(Board(it, null))
                })
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(binding.recyclerView)

            addItemDecoration(GridDividerItemDecoration(4, Color.parseColor("#000000")))
        }
    }

    private fun hasStoragePermission(): Boolean {
        // 권한이 이미 부여되어 있는지 확인
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        // 권한 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_STORAGE_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 부여되면 갤러리 열기
                openGallery()
            } else {
                // 권한이 부여되지 않았을 때 처리
                // 사용자에게 권한이 필요하다고 알릴 수 있습니다.
            }
        }
    }

    companion object {
        private const val REQUEST_STORAGE_PERMISSION = 123
        private const val TAG = "BoardEditFragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}