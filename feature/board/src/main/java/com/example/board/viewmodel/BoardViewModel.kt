package com.example.board.viewmodel

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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
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
            fetchInstagramBoardUseCase(token)
                .catch {
                    _boardUiState.value = BoardUiState.Error(it.message ?: "fetchInstagramBoardUseCase 실패")
                }.collectLatest { items ->
                    _boardUiState.value = BoardUiState.Success(items)
                }
        } else {
            _boardUiState.value = BoardUiState.Error("token is Null Or Empty!!")
        }
    }

    fun requestBoardLocalItem() = viewModelScope.launch {
        _boardLocalUiState.value = BoardLocalUiState.Loading

        findBoardUseCase().catch {
            _boardLocalUiState.value = BoardLocalUiState.Error("requestBoardItemsFind Fail")
        }.collectLatest { items ->
            _boardLocalUiState.value = BoardLocalUiState.Success(items)
        }
    }

    fun requestBoardChildItems(id: String, mediaUrl: String?) = viewModelScope.launch {
        _boardDetailUiState.value = BoardDetailUiState.Loading

        manageUserInformationUseCase.get().also { accessToken ->
            if (!accessToken.isNullOrBlank()) {
                fetchBoardChildItemUseCase(id, accessToken).catch {
                    _boardDetailUiState.value = BoardDetailUiState.Error("boardDetail is Null!!")
                }.collectLatest { boardDetail ->
                    _boardDetailUiState.value = boardDetail.let {
                        if (it.items.size == 0 && !id.isNullOrEmpty() && !mediaUrl.isNullOrEmpty()) {
                            it.items.add(BoardDetail.Item(id = id, mediaUrl = mediaUrl))
                        }
                        BoardDetailUiState.Success(it)
                    }
                }
            } else {
                _boardDetailUiState.value = BoardDetailUiState.Error("accessToken is Error!!")
            }
        }
    }

    private fun requestBoardItemInsert(board: Board) = viewModelScope.launch {
        insertBoardUseCase(board)
    }

    fun requestBoardItemUpdate(board: Board) = viewModelScope.launch {
        updateBoardUseCase(board)
    }

    fun requestBoardItemDelete(item: Board.Item) = viewModelScope.launch {
        deleteBoardUseCase(item)
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
