package com.example.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        _binding = FragmentMainBinding.inflate(inflater, container, false)
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
                mainViewModel.mainUiState.collectLatest { mainUiState ->
                    when (mainUiState) {
                        is MainUiState.Success -> {
                            binding.progressBar.isVisible = false

                            if (mainUiState.isShowBoardFragment) {
                                val request = NavDeepLinkRequest.Builder
                                    .fromUri("app://example.app/boardFragment/${mainUiState.accessToken}".toUri())
                                    .build()
                                findNavController().navigate(request)
                            }
                        }

                        is MainUiState.Error -> {
                            binding.progressBar.isVisible = false

                            when (mainUiState.errorState) {
                                is MainUiState.Error.ErrorState.NetworkError -> {
                                    handleError(mainUiState.errorState.message)
                                }

                                is MainUiState.Error.ErrorState.DefaultError -> {
                                    handleError(mainUiState.errorState.message)
                                }
                            }
                        }
                        MainUiState.Loading -> {
                            binding.progressBar.isVisible = true
                        }
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

    private fun handleError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

        Log.d(TAG, message)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    companion object {
        private const val TAG = "MainFragment"
    }
}
