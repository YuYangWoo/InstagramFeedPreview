package com.example.board.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.model.Board
import com.example.model.BoardDetail
import com.example.usecase.DeleteBoardUseCase
import com.example.usecase.FetchBoardChildItemUseCase
import com.example.usecase.FetchInstagramBoardUseCase
import com.example.usecase.FindBoardUseCase
import com.example.usecase.InsertBoardUseCase
import com.example.usecase.ManageUserInformationUseCase
import com.example.usecase.UpdateBoardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
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
    private val findBoardUseCase: FindBoardUseCase,
    private val updateBoardUseCase: UpdateBoardUseCase,
    private val deleteBoardUseCase: DeleteBoardUseCase
    ) : ViewModel() {
    private val _boardUiState = MutableStateFlow<BoardUiState<PagingData<Board.Item>>>(BoardUiState.Loading)
    val boardUiState = _boardUiState.asStateFlow()

    private val _boardDetailUiState = MutableStateFlow<BoardDetailUiState<BoardDetail>>(BoardDetailUiState.Loading)
    val boardDetailUiState = _boardDetailUiState.asStateFlow()

    fun requestBoardPagingItem(token: String?): Flow<PagingData<Board.Item>>? {
        return token?.let { fetchInstagramBoardUseCase.invoke(it).cachedIn(viewModelScope) }
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

    private suspend fun requestBoardItemInsert(board: Board) = viewModelScope.launch {
        insertBoardUseCase.invoke(board)
    }

    fun requestBoardItemUpdate(board: Board) = viewModelScope.launch {
        updateBoardUseCase.invoke(board)
    }

    private suspend fun requestBoardItemsFind(): List<Board.Item>? {
        return findBoardUseCase.invoke()
    }

    private fun requestBoardItemDelete(item: Board.Item) = viewModelScope.launch {
        deleteBoardUseCase.invoke(item)
    }

    fun requestBoardItemDeleteAndSelect(item: Board.Item) = viewModelScope.launch {
        requestBoardItemDelete(item)
        requestBoardItemsFind()
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
