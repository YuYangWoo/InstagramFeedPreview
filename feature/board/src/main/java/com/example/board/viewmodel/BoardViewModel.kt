package com.example.board.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
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

    private val _boardLocalUiState = MutableStateFlow<BoardLocalUiState<List<Board.Item>>>(BoardLocalUiState.Loading)
    val boardLocalUiState = _boardLocalUiState.asStateFlow()

    private val _boardUiState = MutableStateFlow<BoardUiState<PagingData<Board.Item>>>(BoardUiState.Loading)
    val boardUiState = _boardUiState.asStateFlow()

    private val _boardDetailUiState = MutableStateFlow<BoardDetailUiState<BoardDetail>>(BoardDetailUiState.Loading)
    val boardDetailUiState = _boardDetailUiState.asStateFlow()

    fun requestBoardPagingItem(token: String?) = viewModelScope.launch {
        _boardUiState.value = BoardUiState.Loading
        if (!token.isNullOrEmpty()) {
            runCatching {
                fetchInstagramBoardUseCase.invoke(token)
            }.onSuccess { data ->
                _boardUiState.value = data?.let {
                    Log.d("11111", it.toString())
                    BoardUiState.Success(it)
                } ?: BoardUiState.Error("PaingData<Board.Item> 에러")
            }.onFailure {
                _boardUiState.value = BoardUiState.Error("fetchInstagramBoardUseCase.invoke 함수 실패")
            }
        } else {
            _boardUiState.value = BoardUiState.Error("token is Null Or Empty!!")
        }

    }

    fun requestBoardLocalItem() = viewModelScope.launch {
        _boardLocalUiState.value = BoardLocalUiState.Loading

        runCatching {
            requestBoardItemsFind()
        }.onSuccess { items ->
            _boardLocalUiState.value = items?.let {
                BoardLocalUiState.Success(it)
            } ?: BoardLocalUiState.Error("items is Null or Empty")
        }.onFailure {
            _boardLocalUiState.value = BoardLocalUiState.Error("requestBoardItemsFind Fail")
        }
    }

    fun requestBoardChildItems(id: String, mediaUrl: String?) = viewModelScope.launch {
        manageUserInformationUseCase.get().also { accessToken ->
            if (!accessToken.isNullOrBlank()) {
                _boardDetailUiState.value = BoardDetailUiState.Loading
                runCatching {
                    fetchBoardChildItemUseCase.invoke(id, accessToken)
                }.onFailure {
                    _boardDetailUiState.value = BoardDetailUiState.Error("boardDetail fetch Error!!")
                }.onSuccess { boardDetail ->
                    Log.d("1111", boardDetail.toString())
                    _boardDetailUiState.value = boardDetail?.let {
                        if (it.items.size == 0 && !id.isNullOrEmpty() && !mediaUrl.isNullOrEmpty()) {
                            it.items.add(BoardDetail.Item(id = id, mediaUrl = mediaUrl))
                        }
                        BoardDetailUiState.Success(it)
                    } ?: BoardDetailUiState.Error(
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

    suspend fun requestBoardItemsFind(): List<Board.Item>? {
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

sealed class BoardLocalUiState<out T> {
    object Loading : BoardLocalUiState<Nothing>()
    data class Success<T>(val data: T) : BoardLocalUiState<T>()
    data class Error(val message: String) : BoardLocalUiState<Nothing>()
}

sealed class BoardDetailUiState<out T> {
    object Loading : BoardDetailUiState<Nothing>()
    data class Success<T>(val data: T) : BoardDetailUiState<T>()
    data class Error(val message: String) : BoardDetailUiState<Nothing>()
}
