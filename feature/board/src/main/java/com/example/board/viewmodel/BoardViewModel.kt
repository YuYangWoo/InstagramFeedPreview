package com.example.board.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Board
import com.example.model.BoardDetail
import com.example.usecase.FetchBoardChildItemUseCase
import com.example.usecase.FetchInstagramBoardUseCase
import com.example.usecase.FindBoardUseCase
import com.example.usecase.InsertBoardUseCase
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
    private val insertBoardUseCase: InsertBoardUseCase,
    private val findBoardUseCase: FindBoardUseCase
    ) : ViewModel() {
    private val _boardUiState = MutableStateFlow<BoardUiState<ArrayList<Board.Item>>>(BoardUiState.Loading)
    val boardUiState = _boardUiState.asStateFlow()

    private val _boardDetailUiState = MutableStateFlow<BoardDetailUiState<BoardDetail>>(
        BoardDetailUiState.Loading
    )
    val boardDetailUiState = _boardDetailUiState.asStateFlow()

    fun requestBoardItem() = viewModelScope.launch {
        manageUserInformationUseCase.get().also { accessToken ->
            if (!accessToken.isNullOrBlank()) {
                runCatching {
                    fetchInstagramBoardUseCase.invoke(accessToken)
                }.onFailure {
                    _boardUiState.value = BoardUiState.Error("board fetch Error!!")
                }.onSuccess { board ->
                    if (board != null) {
                        findBoardItem(board)
                    } else {
                        _boardUiState.value = BoardUiState.Error("board is nullOrEmpty")
                    }
                }
            } else {
                _boardUiState.value = BoardUiState.Error("accessToken is nullOrEmpty")
            }
        }
    }

    private fun findBoardItem(board: Board) = viewModelScope.launch {
        runCatching {
            insertBoardUseCase.invoke(board)
        }.onSuccess {
            _boardUiState.value = findBoardUseCase.invoke()?.let {
                Log.d(TAG, it.toString())
                BoardUiState.Success(it)
            } ?: BoardUiState.Error(
                "board is Null!!"
            )
        }.onFailure {
            Log.d(TAG, "insertBoardUseCase.invoke() 실패")
        }
    }

    fun requestBoardChildItems(mediaId: String) = viewModelScope.launch {
        manageUserInformationUseCase.get().also { accessToken ->
            if (!accessToken.isNullOrBlank()) {
                _boardDetailUiState.value = BoardDetailUiState.Loading
                runCatching {
                    fetchBoardChildItemUseCase.invoke(mediaId, accessToken)
                }.onFailure {
                    _boardDetailUiState.value = BoardDetailUiState.Error("boardDetail fetch Error!!")
                }.onSuccess { boardDetail ->
                    _boardDetailUiState.value = boardDetail?.let { BoardDetailUiState.Success(it) } ?: BoardDetailUiState.Error(
                        "boardDetail is Null!!"
                    )
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
