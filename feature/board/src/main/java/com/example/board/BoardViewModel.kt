package com.example.board

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Board
import com.example.usecase.FetchInstagramBoardUseCase
import com.example.usecase.ManageUserInformationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val manageUserInformationUseCase: ManageUserInformationUseCase,
    private val fetchInstagramBoardUseCase: FetchInstagramBoardUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<Board>>(UiState.Empty)
    val uiState: StateFlow<UiState<Board>> = _uiState.asStateFlow()

    fun requestBoardItem() = viewModelScope.launch {
        manageUserInformationUseCase.get().also { accessToken ->
            if (!accessToken.isNullOrBlank()) {
                _uiState.value = UiState.Loading
                runCatching {
                    fetchInstagramBoardUseCase.invoke(accessToken)
                }.onFailure {
                    _uiState.value = UiState.Error("board fetch Error!!")
                }
                .onSuccess { board ->
                    Log.d(TAG, board.toString())
                    _uiState.value = board?.let { UiState.Success(it) } ?: UiState.Error("board is Null!!")
                }
            } else {
                _uiState.value = UiState.Error("accessToken is nullOrEmpty")
            }
        }
    }

    companion object {
        private const val TAG = "BoardViewModel"
    }
}

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
    object Empty : UiState<Nothing>()
}
