package com.example.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Login
import com.example.model.LongToken
import com.example.usecase.FetchInstagramTokenUseCase
import com.example.usecase.SaveUserAccessTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val fetchInstagramTokenUseCase: FetchInstagramTokenUseCase,
    private val saveUserAccessTokenUseCase: SaveUserAccessTokenUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<LongToken>>(UiState.Empty)
    val uiState: StateFlow<UiState<LongToken>> = _uiState.asStateFlow()

    fun requestAccessToken(
        login: Login
    ) = viewModelScope.launch {
        _uiState.value = UiState.Loading

        fetchInstagramTokenUseCase(login).catch {
            _uiState.value = UiState.Error("token fetch Error!!${it.message.toString()}")
        }.collectLatest { longTokenEntity ->
            _uiState.value = longTokenEntity.let {
                saveUserAccessToken(it.accessToken)
                UiState.Success(it)
            }
        }
    }

    private fun saveUserAccessToken(accessToken: String) = viewModelScope.launch {
        saveUserAccessTokenUseCase(accessToken)
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    object Empty : UiState<Nothing>()
}
