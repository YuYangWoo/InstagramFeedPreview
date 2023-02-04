package com.example.instagramfeedpreview.ui.component

import com.example.instagramfeedpreview.R
import com.example.instagramfeedpreview.databinding.ActivityMainBinding
import com.example.library.navigation.NavigationActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : NavigationActivity<ActivityMainBinding>(R.layout.activity_main, R.id.fragmentContainerView) {

    override fun init() {
        super.init()
        setSupportActionBar(binding.toolbar)
    }
}
