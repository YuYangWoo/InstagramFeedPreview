package com.example.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.usecase.FetchUserAccessTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    fetchUserAccessTokenUseCase: FetchUserAccessTokenUseCase
) : ViewModel() {
    val mainUiState = fetchUserAccessTokenUseCase().map {
        if (it.isEmpty()) {
            MainUiState(isLoading = false, isShowBoardFragment = false, accessToken = it)
        } else {
            MainUiState(isLoading = false, isShowBoardFragment = true, accessToken = it)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MainUiState(isLoading = true, isShowBoardFragment = false)
    )
}
