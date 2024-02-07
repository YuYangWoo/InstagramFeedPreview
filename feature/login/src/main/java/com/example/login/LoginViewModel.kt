package com.example.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.LoginEntity
import com.example.model.LongTokenEntity
import com.example.usecase.FetchInstagramTokenUseCase
import com.example.usecase.ManageUserInformationUseCase
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
    private val manageUserInformationUseCase: ManageUserInformationUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<LongTokenEntity>>(UiState.Empty)
    val uiState: StateFlow<UiState<LongTokenEntity>> = _uiState.asStateFlow()

    fun requestAccessToken(
        loginEntity: LoginEntity
    ) = viewModelScope.launch {
        _uiState.value = UiState.Loading

        fetchInstagramTokenUseCase(loginEntity).catch {
            _uiState.value = UiState.Error("token fetch Error!!${it.message.toString()}")
        }.collectLatest { longTokenEntity ->
            _uiState.value = longTokenEntity.let {
                saveUserAccessToken(it.accessToken)
                UiState.Success(it)
            }
        }
    }

    private fun saveUserAccessToken(accessToken: String) = viewModelScope.launch {
        manageUserInformationUseCase.save(accessToken)
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
