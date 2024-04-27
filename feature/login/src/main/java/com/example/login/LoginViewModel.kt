package com.example.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.login.event.Contract
import com.example.login.state.LoginUiState
import com.example.model.Login
import com.example.usecase.FetchInstagramTokenUseCase
import com.example.usecase.SaveUserAccessTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val fetchInstagramTokenUseCase: FetchInstagramTokenUseCase,
    private val saveUserAccessTokenUseCase: SaveUserAccessTokenUseCase
) : ViewModel() {

    private val _loginUiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState.asStateFlow()

    private fun requestAccessToken(login: Login) = viewModelScope.launch {
        fetchInstagramTokenUseCase(login)
            .onStart {
                _loginUiState.update { loginUiState ->
                    loginUiState.copy(
                        isLoading = true,
                    )
                }
            }.collectLatest { longToken ->
                _loginUiState.update { loginUiState ->
                    loginUiState.copy(
                        isLoading = false,
                        isShowBoardFragment = longToken.accessToken.isNotEmpty(),
                        accessToken = longToken.accessToken
                    )
                }
            }
    }

    fun event(event: Contract.Event) {
        when (event) {
            is Contract.Event.RequestAccessToken -> {
                requestAccessToken(event.login)
            }
            is Contract.Event.SaveUserAccessToken -> saveUserAccessToken(event.accessToken)
        }
    }

    private fun saveUserAccessToken(accessToken: String) = viewModelScope.launch {
        saveUserAccessTokenUseCase(accessToken)
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
