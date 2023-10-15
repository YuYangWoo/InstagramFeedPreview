package com.example.board

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Board
import com.example.model.BoardDetail
import com.example.usecase.FetchBoardChildItemUseCase
import com.example.usecase.FetchInstagramBoardUseCase
import com.example.usecase.ManageUserInformationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val manageUserInformationUseCase: ManageUserInformationUseCase,
    private val fetchInstagramBoardUseCase: FetchInstagramBoardUseCase,
    private val fetchBoardChildItemUseCase: FetchBoardChildItemUseCase,
    ) : ViewModel() {
    private val _boardUiState = MutableStateFlow<BoardUiState<Board>>(BoardUiState.Loading)
    val boardUiState = _boardUiState.asStateFlow()

    private val _boardDetailUiState = MutableStateFlow<BoardDetailUiState<BoardDetail>>(BoardDetailUiState.Loading)
    val boardDetailUiState = _boardDetailUiState.asStateFlow()

    fun requestBoardItem() = viewModelScope.launch {
        manageUserInformationUseCase.get().also { accessToken ->
            Log.d(TAG, "accessToken is $accessToken")
            if (!accessToken.isNullOrBlank()) {
                runCatching {
                    fetchInstagramBoardUseCase.invoke(accessToken)
                }.onFailure {
                    _boardUiState.value = BoardUiState.Error("board fetch Error!!")
                }
                .onSuccess { board ->
                    Log.d(TAG, board.toString())
                    _boardUiState.value = board?.let { BoardUiState.Success(it) } ?: BoardUiState.Error("board is Null!!")
                }
            } else {
                _boardUiState.value = BoardUiState.Error("accessToken is nullOrEmpty")
            }
        }
    }

    fun requestBoardChildItems(mediaId: String) = viewModelScope.launch {
        manageUserInformationUseCase.get().also { accessToken ->
            if (!accessToken.isNullOrBlank()) {
                _boardDetailUiState.value = BoardDetailUiState.Loading
                runCatching {
                    fetchBoardChildItemUseCase.invoke(mediaId, accessToken)
                }.onFailure {
                    _boardDetailUiState.value = BoardDetailUiState.Error("boardChild fetch Error!!")
                }.onSuccess { boardChild ->
                    _boardDetailUiState.value = boardChild?.let { BoardDetailUiState.Success(it) } ?: BoardDetailUiState.Error("boardChild is Null!!")
                }
            } else {
                _boardDetailUiState.value = BoardDetailUiState.Error("accessToken is Error!!")
            }
        }
    }

    companion object {
        private const val TAG = "BoardViewModel"
    }

}
sealed class BoardUiState<out T> {
    object Loading : BoardUiState<Nothing>()
    data class Success<T>(val data: T) : BoardUiState<T>()
    data class Error(val message: String) : BoardUiState<Nothing>()
}

sealed class BoardDetailUiState<out T> {
    object Loading : BoardDetailUiState<Nothing>()
    data class Success<T>(val data: T) : BoardDetailUiState<T>()
    data class Error(val message: String) : BoardDetailUiState<Nothing>()
}
