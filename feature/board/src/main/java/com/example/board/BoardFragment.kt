package com.example.board

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.board.databinding.FragmentBoardBinding
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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBoardBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        requestBoardItems()
        initObserver()
        initSwipeRefreshLayout()
    }

    private fun initSwipeRefreshLayout() {
        binding.swipeRefreshLayout.apply {
            setOnRefreshListener {
                lifecycleScope.launchWhenCreated {
                    requestBoardItems()
                }
                isRefreshing = false
            }
        }
    }

    private fun requestBoardItems() {
        boardViewModel.requestBoardItem()
    }

    private fun initRecyclerView() {
        with (binding.feedRecyclerView) {
            adapter = boardAdapter.apply {
                setOnItemClickListener {
                    boardViewModel.requestBoardChildItems(it.id)
                    val request = NavDeepLinkRequest.Builder
                        .fromUri("app://example.app/boardDetailFragment".toUri())
                        .build()
                    findNavController().navigate(request)
                }
            }
            layoutManager = GridLayoutManager(context, 3)
            addItemDecoration(GridDividerItemDecoration(4, android.graphics.Color.parseColor("#000000")))
        }
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                boardViewModel.boardUiState.collectLatest { state ->
                    when (state) {
                        is BoardUiState.Success -> {
                            Log.d(TAG, "success")
                            binding.progressBar.isVisible = false
                            boardAdapter.submitList(state.data.items)
                        }
                        is BoardUiState.Error -> {
                            Log.d(TAG, "Error")
                            binding.progressBar.isVisible = false
                            Log.d(TAG, state.message)
                        }
                        is BoardUiState.Loading -> {
                            Log.d(TAG, "loading")
                            binding.progressBar.isVisible = true
                        }
                    }
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
