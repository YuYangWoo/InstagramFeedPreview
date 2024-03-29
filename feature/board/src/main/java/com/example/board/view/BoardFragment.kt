package com.example.board.view

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.board.GridDividerItemDecoration
import com.example.board.R
import com.example.board.adapter.BoardAdapter
import com.example.board.adapter.BoardLoadStateAdapter
import com.example.board.databinding.FragmentBoardBinding
import com.example.board.viewmodel.BoardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BoardFragment : Fragment(R.layout.fragment_board) {
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
        _binding = FragmentBoardBinding.inflate(inflater, container, false)

        initMenu()

        return binding.root
    }

    private fun initMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.editButton -> {
                        BoardEditFragment().show(childFragmentManager, "BoardEditFragment")
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = arguments?.getString("accessToken")
        boardViewModel.setToken(token ?: "")

        initRecyclerView()
        initObserver()
    }

    private fun initRecyclerView() {
        with(binding.feedRecyclerView) {
            adapter = boardAdapter.apply {
                setOnItemClickListener { board, position ->
                    boardViewModel.requestBoardDetailItem(board.id, board.mediaUrl)
                    val request =
                        NavDeepLinkRequest.Builder.fromUri("app://example.app/boardDetailFragment".toUri())
                            .build()
                    findNavController().navigate(request)
                }
            }
            adapter = boardAdapter.withLoadStateFooter(BoardLoadStateAdapter(boardAdapter::retry))

            layoutManager = GridLayoutManager(context, 3)

            addItemDecoration(GridDividerItemDecoration(4, Color.parseColor("#000000")))
        }
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                boardViewModel.pagingData.collectLatest {
                    boardAdapter.submitData(it)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (System.currentTimeMillis() > backKeyPressedTime + LIMIT_TIME) {
                        backKeyPressedTime = System.currentTimeMillis()
                        Toast.makeText(
                            requireContext(),
                            "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.",
                            Toast.LENGTH_SHORT
                        ).show()
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
