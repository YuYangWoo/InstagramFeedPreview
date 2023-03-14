package com.example.instagramfeedpreview.ui.component.main

import androidx.lifecycle.ViewModel
import com.example.instagramfeedpreview.data.repository.instagramRepository.InstagramRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val tokenRepository: InstagramRepository) : ViewModel(){

}
