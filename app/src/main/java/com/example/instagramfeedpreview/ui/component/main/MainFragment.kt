package com.example.instagramfeedpreview.ui.component.main

import androidx.navigation.fragment.findNavController
import com.example.instagramfeedpreview.R
import com.example.instagramfeedpreview.databinding.FragmentMainBinding
import com.example.library.binding.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : BindingFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override fun init() {
        super.init()
        initClickListener()
    }

    private fun initClickListener() {
        binding.loginButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToInstagramFragment())
        }
    }

    companion object {
        private const val TAG = "MainFragment"
    }
}
