package com.example.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.example.main.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private val mainViewModel: MainViewModel by viewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initCollect()
    }

    private fun initCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.mainUiState.collectLatest { state ->
                    binding.progressBar.isVisible = state.isLoading
                    if (state.isShowBoardFragment) {
                        val request = NavDeepLinkRequest.Builder
                            .fromUri("app://example.app/boardFragment/${state.accessToken}".toUri())
                            .build()
                        findNavController().navigate(request)
                    }
                }
            }
        }
    }

    private fun init() {
        binding.loginButton.setOnClickListener {
            val request = NavDeepLinkRequest.Builder
                .fromUri("app://example.app/loginFragment".toUri())
                .build()
            findNavController().navigate(request)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    companion object {
        private const val TAG = "MainFragment"
    }
}
