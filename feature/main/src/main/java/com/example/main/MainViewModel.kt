package com.example.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.usecase.ManageUserInformationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val manageUserInformationUseCase: ManageUserInformationUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<String>>(UiState.Empty)
    val uiState = _uiState.asStateFlow()

    fun getUserAccessToken() =
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            runCatching {
                manageUserInformationUseCase.get()
            }.onFailure { _uiState.value = UiState.Error("getUserAccessToken method is Fail!!") }
                .onSuccess {
                    _uiState.value = if (!it.isNullOrBlank()) {
                        UiState.Success(it)
                    } else {
                        UiState.Error("userToken is nullOrBlank!!")
                    }
                }
        }
}

sealed class UiState<out T> {
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    object Empty : UiState<Nothing>()
}