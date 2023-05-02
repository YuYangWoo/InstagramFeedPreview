package com.example.main

import com.example.library.navigation.NavigationActivity
import com.example.main.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : NavigationActivity<ActivityMainBinding>(R.layout.activity_main, R.id.fragmentContainerView) {

    override fun init() {
        super.init()
        setSupportActionBar(binding.toolbar)
    }
}
