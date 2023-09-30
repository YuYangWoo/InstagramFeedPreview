package com.example.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Board
import com.example.usecase.FetchInstagramBoardUseCase
import com.example.usecase.HandleUserInformationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val handleUserInformationUseCase: HandleUserInformationUseCase,
    private val fetchInstagramBoardUseCase: FetchInstagramBoardUseCase,
    ) : ViewModel() {
    private val _uiState = MutableStateFlow<UIState>(UIState.Empty)
    val uiState: StateFlow<UIState> = _uiState

    private val _accessToken = MutableStateFlow("")
    val accessToken: StateFlow<String> = _accessToken

    private val _boardDTO = MutableSharedFlow<Board>()
    val boardDTO: SharedFlow<Board> = _boardDTO

    fun getUserAccessToken() = viewModelScope.launch {
        _uiState.value = UIState.Loading
        try {
            handleUserInformationUseCase.get()?.let { accessToken ->
                _uiState.value = UIState.Success
                _accessToken.emit(accessToken)
            }
        } catch (e: Exception) {
            _uiState.value = UIState.Error(e)
        }
    }

    fun requestBoardItem(
        accessToken: String
    ) = viewModelScope.launch {
        _uiState.value = UIState.Loading

        try {
            fetchInstagramBoardUseCase.invoke(accessToken)?.let { boardDTO ->
                _uiState.value = UIState.Success
                _boardDTO.emit(boardDTO)
            }
        } catch (e: Exception) {
            _uiState.value = UIState.Error(e)
        }
    }
}

sealed class UIState {
    object Success: UIState()
    object Loading: UIState()
    data class Error(val e: Exception): UIState()
    object Empty: UIState()
}

