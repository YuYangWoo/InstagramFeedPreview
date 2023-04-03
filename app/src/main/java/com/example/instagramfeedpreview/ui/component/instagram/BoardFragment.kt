package com.example.instagramfeedpreview.ui.component.instagram

import android.content.Context
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.instagramfeedpreview.R
import com.example.instagramfeedpreview.databinding.FragmentBoardBinding
import com.example.instagramfeedpreview.ui.component.instagram.adapter.FeedAdapter
import com.example.instagramfeedpreview.ui.component.instagram.adapter.GridDividerItemDecoration
import com.example.library.binding.BindingFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class BoardFragment : BindingFragment<FragmentBoardBinding>(R.layout.fragment_board){
    private val instagramViewModel by activityViewModels<InstagramViewModel>()
    private var backKeyPressedTime: Long = 0
    private val args: BoardFragmentArgs by navArgs()

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
                instagramViewModel.requestAccessToken(args.loginDTO)
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
            instagramViewModel.boardDTO.collectLatest { boardDTO ->
                feedAdapter.submitList(boardDTO.data)
            }
        }

        lifecycleScope.launchWhenCreated {
            instagramViewModel.uiState.collectLatest {
                when (it) {
                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                    }
                    is UiState.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is UiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Log.d(TAG, it.e.message.toString())
                    }
                    is UiState.Empty -> Unit
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
