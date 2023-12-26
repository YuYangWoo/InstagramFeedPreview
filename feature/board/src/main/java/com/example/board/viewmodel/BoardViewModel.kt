package com.example.board.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.model.Board
import com.example.model.BoardDetail
import com.example.model.LocalBoard
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

    private val _boardLocalUiState = MutableStateFlow<BoardLocalUiState<List<LocalBoard.Item>>>(BoardLocalUiState.Loading)
    val boardLocalUiState = _boardLocalUiState.asStateFlow()

    private val _boardUiState = MutableStateFlow<BoardUiState<PagingData<Board.Item>>>(BoardUiState.Loading)
    val boardUiState = _boardUiState.asStateFlow()

    private val _boardDetailUiState = MutableStateFlow<BoardDetailUiState<BoardDetail>>(BoardDetailUiState.Loading)
    val boardDetailUiState = _boardDetailUiState.asStateFlow()

    fun requestBoardPagingItem(token: String?): Flow<PagingData<Board.Item>>? {
        return token?.let { fetchInstagramBoardUseCase.invoke(it).cachedIn(viewModelScope) }
    }

    fun requestBoardLocalItem() = viewModelScope.launch {
        _boardLocalUiState.value = BoardLocalUiState.Loading

        findBoardUseCase().catch {
            _boardLocalUiState.value = BoardLocalUiState.Error("requestBoardItemsFind Fail")
        }.collectLatest { items ->
            _boardLocalUiState.value = BoardLocalUiState.Success(items)
        }
    }

    fun requestBoardDetailItem(id: String, mediaUrl: String?) = viewModelScope.launch {
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

    fun insertBoardItem(localBoard: LocalBoard) = viewModelScope.launch {
        insertBoardUseCase(localBoard)
    }
    fun insertAdditionalBoardItem(localBoard: LocalBoard) = viewModelScope.launch {
        insertBoardUseCase.invokeAdditional(localBoard)
    }

    fun updateBoardItem(localBoard: LocalBoard) = viewModelScope.launch {
        updateBoardUseCase(localBoard)
    }

    fun deleteBoardItem(localBoardItem: LocalBoard.Item) = viewModelScope.launch {
        deleteBoardUseCase(localBoardItem)
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
