package com.example.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Login
import com.example.model.Token
import com.example.usecase.FetchInstagramBoardUseCase
import com.example.usecase.FetchInstagramTokenUseCase
import com.example.usecase.ManageUserInformationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InstagramViewModel @Inject constructor(
    private val fetchInstagramBoardUseCase: FetchInstagramBoardUseCase,
    private val fetchInstagramTokenUseCase: FetchInstagramTokenUseCase,
    private val manageUserInformationUseCase: ManageUserInformationUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<Token>>(UiState.Empty)
    val uiState: StateFlow<UiState<Token>> = _uiState.asStateFlow()

//    private val _boardDTO = MutableSharedFlow<Board>()
//    val boardDTO: SharedFlow<Board> = _boardDTO
//
//    private val _token = MutableStateFlow<Token>(null)
//    val token: SharedFlow<Token> = _token
//
//    private val _accessToken = MutableStateFlow("")
//    val accessToken: StateFlow<String> = _accessToken

    fun requestAccessToken(
        login: Login
    ) = viewModelScope.launch {
        runCatching { fetchInstagramTokenUseCase.invoke(login) }
            .onFailure {
                _uiState.value = UiState.Error("token fetch Error!!")
            }
            .onSuccess { token ->
                _uiState.value = token?.let {
                    saveUserAccessToken(it.accessToken)
                    UiState.Success(it)
                } ?: UiState.Error("token is Null!!")
            }
    }

//    fun requestBoardItem(
//        accessToken: String
//    ) = viewModelScope.launch {
//        _uiState.value = UiState.Loading
//
//        try {
//            fetchInstagramBoardUseCase.invoke(accessToken)?.let { boardDTO ->
//                _uiState.value = UiState.Success
//                _boardDTO.emit(boardDTO)
//            }
//        } catch (e: Exception) {
//            _uiState.value = UiState.Error(e)
//        }
//    }

    private fun saveUserAccessToken(accessToken: String) = viewModelScope.launch {
        manageUserInformationUseCase.save(accessToken)
    }

    companion object {
        private const val TAG = "InstagramViewModel"
    }
}

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    object Empty : UiState<Nothing>()
}
