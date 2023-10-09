package com.example.main

import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.example.library.binding.BindingFragment
import com.example.main.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BindingFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val mainViewModel: MainViewModel by activityViewModels()
    override fun init() {
        super.init()
        initClickListener()
        initObserver()
        checkUserToken()
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.uiState.collectLatest { state ->
                    when (state) {
                        is UiState.Success -> {
                            binding.progressBar.isVisible = false
                            val request = NavDeepLinkRequest.Builder
                                .fromUri("app://example.app/boardFragment".toUri())
                                .build()
                            findNavController().navigate(request)
                        }
                        is UiState.Error -> {
                            binding.progressBar.isVisible = false
                        }
                        is UiState.Loading -> {
                            binding.progressBar.isVisible = true
                        }
                        is UiState.Empty -> {

                        }
                    }
                }
            }
        }
    }

    private fun checkUserToken() {
        mainViewModel.getUserAccessToken()
    }

    private fun initClickListener() {
        binding.loginButton.setOnClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri("app://example.app/loginFragment".toUri())
                .build()
            findNavController().navigate(request)
        }
    }

    companion object {
        private const val TAG = "MainFragment"
    }
}
