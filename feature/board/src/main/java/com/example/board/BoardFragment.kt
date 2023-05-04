package com.example.board

import android.content.Context
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.board.databinding.FragmentBoardBinding
import com.example.library.binding.BindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class BoardFragment : BindingFragment<FragmentBoardBinding>(R.layout.fragment_board){
    private val boardViewModel: BoardViewModel by activityViewModels()
    private var backKeyPressedTime: Long = 0

    @Inject
    lateinit var feedAdapter: FeedAdapter

    override fun init() {
        super.init()
        initRecyclerView()
        initObserver()
        initSwipeRefreshLayout()
    }

    private fun initSwipeRefreshLayout() {
        binding.swipeRefreshLayout.apply {
            setOnRefreshListener {
                lifecycleScope.launchWhenCreated {
                    boardViewModel.getUserAccessToken()
                }
                isRefreshing = false
            }
        }
    }

    private fun initRecyclerView() {
        with (binding.feedRecyclerView) {
            adapter = feedAdapter
            layoutManager = GridLayoutManager(context, 3)
            addItemDecoration(GridDividerItemDecoration(4, android.graphics.Color.parseColor("#000000")))
        }
    }

    private fun initObserver() {
        lifecycleScope.launchWhenCreated {
            boardViewModel.boardDTO.collectLatest { boardDTO ->
                feedAdapter.submitList(boardDTO.data)
            }
        }

        lifecycleScope.launchWhenCreated {
            boardViewModel.uiState.collectLatest {
                when (it) {
                    is UIState.Success -> {
                        binding.progressBar.visibility = View.GONE
                    }
                    is UIState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is UIState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Log.d(TAG, it.e.message.toString())
                    }
                    is UIState.Empty -> Unit
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            boardViewModel.accessToken.collectLatest { accessToken ->
                boardViewModel.requestBoardItem(accessToken)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() > backKeyPressedTime + LIMIT_TIME) {
                    backKeyPressedTime = System.currentTimeMillis()
                    shortToast(requireContext(), "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.")
                    return
                }

                if (System.currentTimeMillis() <= backKeyPressedTime + LIMIT_TIME) {
                    requireActivity().finish()
                }
            }
        })
    }

    companion object {
        private const val LIMIT_TIME = 2000
        private const val TAG = "BoardFragment"
    }
}
