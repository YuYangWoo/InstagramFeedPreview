package com.example.board.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.board.GridDividerItemDecoration
import com.example.board.ItemMoveCallback
import com.example.board.R
import com.example.board.adapter.BoardAdapter
import com.example.board.databinding.FragmentBoardBinding
import com.example.board.viewmodel.BoardViewModel
import com.example.model.Board
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BoardFragment : Fragment(R.layout.fragment_board){
    private val boardViewModel: BoardViewModel by activityViewModels()
    private var backKeyPressedTime: Long = 0

    @Inject
    lateinit var boardAdapter: BoardAdapter
    private var _binding: FragmentBoardBinding? = null
    private val binding get() = _binding!!
    private var token: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        token = arguments?.getString("accessToken")

        initRecyclerView()
        initObserver()
        initSwipeRefreshLayout(token)
        initClickListener()
    }

    private fun initClickListener() {
        binding.trashCanImageView.setOnClickListener {
            Toast.makeText(requireContext(), "삭제할 사진을 선택해주세요.", Toast.LENGTH_SHORT).show()
            binding.trashCanImageView.tag = if (binding.trashCanImageView.tag == true) {
                binding.trashCanImageView.isSelected = false
                false
            } else {
                binding.trashCanImageView.isSelected = true
                true
            }
        }
    }

    private fun initSwipeRefreshLayout(token: String?) {
        binding.swipeRefreshLayout.apply {
            setOnRefreshListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        boardViewModel.requestBoardPagingItem(token)?.collectLatest {
                            boardAdapter.submitData(it)
                        }
                    }
                }
                isRefreshing = false
            }
        }
    }

    private fun initRecyclerView() {
        with (binding.feedRecyclerView) {
            adapter = boardAdapter.apply {
                setOnItemClickListener { board, position ->
                    when (binding.trashCanImageView.tag) {
                        true -> {
                            boardViewModel.requestBoardItemDeleteAndSelect(board)
                            boardAdapter.notifyItemRemoved(position)
                        }
                        else -> {
                            boardViewModel.requestBoardChildItems(board.id)
                            val request = NavDeepLinkRequest.Builder
                                .fromUri("app://example.app/boardDetailFragment".toUri())
                                .build()
                            findNavController().navigate(request)
                        }
                    }

                }
            }

            layoutManager = GridLayoutManager(context, 3)

            val callback = ItemMoveCallback(
                boardAdapter = boardAdapter,
                onCompleteListener = {
                    boardViewModel.requestBoardItemUpdate(Board(it, null))
                },
                onSelectedChangedListener = {
                    binding.swipeRefreshLayout.isEnabled = binding.swipeRefreshLayout.isEnabled.not()
                }
            )
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(binding.feedRecyclerView)

            addItemDecoration(GridDividerItemDecoration(4, Color.parseColor("#000000")))
        }
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                boardViewModel.requestBoardPagingItem(token)?.collectLatest {
                    boardAdapter.submitData(it)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() > backKeyPressedTime + LIMIT_TIME) {
                    backKeyPressedTime = System.currentTimeMillis()
                    Toast.makeText(requireContext(), "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
                    return
                }

                if (System.currentTimeMillis() <= backKeyPressedTime + LIMIT_TIME) {
                    requireActivity().finish()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val LIMIT_TIME = 2000
        private const val TAG = "BoardFragment"
    }
}
