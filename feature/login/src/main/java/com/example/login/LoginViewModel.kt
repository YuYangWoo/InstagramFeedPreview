package com.example.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.login.event.Contract
import com.example.login.state.LoginUiState
import com.example.model.Login
import com.example.usecase.FetchInstagramTokenUseCase
import com.example.usecase.SaveUserAccessTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val fetchInstagramTokenUseCase: FetchInstagramTokenUseCase,
    private val saveUserAccessTokenUseCase: SaveUserAccessTokenUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val loginUiState = savedStateHandle.getStateFlow(ARGS_LOGIN_KEY, UiLogin("", "", "", "", ""))
        .flatMapLatest { uiLogin ->
            if (uiLogin.code.isEmpty()) {
                flowOf(LoginUiState.Idle)
            } else {
                loginUiState(uiLogin.toLogin())
            }
        }.stateIn(
        scope = viewModelScope, started = SharingStarted.Eagerly, initialValue = LoginUiState.Idle
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loginUiState(login: Login) =
        fetchInstagramTokenUseCase(login).onStart {
            LoginUiState.Loading
        }.mapLatest {
            LoginUiState.Success(
                LoginUiState.Success.LoginState(
                    isShowBoardFragment = it.accessToken.isNotEmpty(),
                    accessToken = it.accessToken
                )
            )
        }.catch {
            onException(it)
        }

    fun event(event: Contract.Event) {
        when (event) {
            is Contract.Event.SaveUserAccessToken -> saveUserAccessToken(event.accessToken)
            is Contract.Event.OnUpdateLoginInfo -> {
                savedStateHandle["login"] = event.login
            }
        }
    }

    private fun onException(throwable: Throwable): LoginUiState.Error.ErrorState {
        return if (throwable is IOException) {
            LoginUiState.Error.ErrorState.NetworkError(
                "네트워크에 접속할 수 없습니다. 네트워크 연결상태 확인 후 다시 시도해 주세요.", throwable
            )
        } else {
            LoginUiState.Error.ErrorState.DefaultError(
                "알 수 없는 오류입니다. 잠시 후 다시 시도해 주세요.", throwable
            )
        }
    }

    private fun saveUserAccessToken(accessToken: String) = viewModelScope.launch {
        saveUserAccessTokenUseCase(accessToken)
    }

    companion object {
        private const val TAG = "LoginViewModel"
        private const val ARGS_LOGIN_KEY = "login"
    }
}
