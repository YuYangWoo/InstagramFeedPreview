package com.example.main

import androidx.core.net.toUri
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.example.board.BoardViewModel
import com.example.library.binding.BindingFragment
import com.example.main.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BindingFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val boardViewModel: BoardViewModel by activityViewModels()

    override fun init() {
        super.init()
        initClickListener()
//        checkUserAuth()
//        initObserver()
    }

//    private fun initObserver() {
//        lifecycleScope.launchWhenCreated {
//            boardViewModel.accessToken.collectLatest { accessToken ->
//                if (accessToken.isNotBlank()) {
//                    boardViewModel.requestBoardItem(accessToken)
//                    findNavController().navigate(MainFragmentDirections.actionMainFragmentToFeatureBoardNavigation())
//                }
//            }
//        }
//    }

    private fun initClickListener() {
        binding.loginButton.setOnClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri("android-app://example.app/loginFragment".toUri())
                .build()
            findNavController().navigate(request)
        }
    }

//    private fun checkUserAuth() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            boardViewModel.getUserAccessToken()
//        }
//    }

    companion object {
        private const val TAG = "MainFragment"
    }
}
