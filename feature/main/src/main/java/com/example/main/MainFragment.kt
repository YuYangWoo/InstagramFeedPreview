package com.example.main

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.library.binding.BindingFragment
import com.example.main.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BindingFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val instagramViewModel: InstagramViewModel by activityViewModels()

    override fun init() {
        super.init()
        initClickListener()
        checkUserAuth()
        initObserver()
    }

    private fun initObserver() {
        lifecycleScope.launchWhenCreated {
            instagramViewModel.accessToken.collectLatest { accessToken ->
                if (accessToken.isNotBlank()) {
                    instagramViewModel.requestBoardItem(accessToken)
                    findNavController().navigate(MainFragmentDirections.actionMainFragmentToBoardFragment())
                }
            }
        }
    }

    private fun initClickListener() {
        binding.loginButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToInstagramFragment())
        }
    }

    private fun checkUserAuth() {
        viewLifecycleOwner.lifecycleScope.launch {
            instagramViewModel.getUserAccessToken()
        }
    }

    companion object {
        private const val TAG = "MainFragment"
    }
}
