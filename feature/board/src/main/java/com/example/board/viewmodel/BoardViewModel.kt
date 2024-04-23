package com.example.board.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.board.state.BoardDetailUiState
import com.example.model.LocalBoard
import com.example.usecase.DeleteBoardUseCase
import com.example.usecase.FetchBoardDetailItemUseCase
import com.example.usecase.FetchInstagramBoardUseCase
import com.example.usecase.FindBoardUseCase
import com.example.usecase.InsertBoardUseCase
import com.example.usecase.UpdateBoardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val fetchInstagramBoardUseCase: FetchInstagramBoardUseCase,
    private val fetchBoardDetailItemUseCase: FetchBoardDetailItemUseCase,
    private val insertBoardUseCase: InsertBoardUseCase,
    private val findBoardUseCase: FindBoardUseCase,
    private val updateBoardUseCase: UpdateBoardUseCase,
    private val deleteBoardUseCase: DeleteBoardUseCase,
    private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {

    private val _boardLocalUiState = MutableStateFlow<BoardLocalUiState<List<LocalBoard.Item>>>(BoardLocalUiState.Loading)
    val boardLocalUiState = _boardLocalUiState.asStateFlow()

    val boardDetailUiState = combine(
        savedStateHandle.getStateFlow("id", ""),
        savedStateHandle.getStateFlow("mediaUrl", "")
    ) { id, mediaUrl ->
        id to mediaUrl
    }.flatMapLatest { (id, mediaUrl) ->
        fetchBoardDetailItemUseCase(id).map { boardDetail ->
            val items =
                if (boardDetail.items.isEmpty() && id.isNotEmpty() && mediaUrl.isNotEmpty()) {
                    listOf(BoardDetailUiState.Item(id = id, mediaUrl = mediaUrl))
                } else {
                    boardDetail.items.map { item ->
                        BoardDetailUiState.Item(item.id, item.mediaUrl)
                    }
                }
            BoardDetailUiState(isLoading = false, items = items)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = BoardDetailUiState(isLoading = true, items = emptyList())
    )

    private val token = savedStateHandle.getStateFlow("token", "")

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingData = token.flatMapLatest {
        fetchInstagramBoardUseCase(it)
    }.cachedIn(viewModelScope)

    fun requestBoardLocalItem() = viewModelScope.launch {
        _boardLocalUiState.value = BoardLocalUiState.Loading

        findBoardUseCase().catch {
            _boardLocalUiState.value = BoardLocalUiState.Error("requestBoardItemsFind Fail")
        }.collectLatest { items ->
            _boardLocalUiState.value = BoardLocalUiState.Success(items)
        }
    }

    fun insertAdditionalBoardItem(localBoard: LocalBoard) = viewModelScope.launch {
        insertBoardUseCase.invoke(localBoard)
    }

    fun updateBoardItem(localBoard: LocalBoard) = viewModelScope.launch {
        updateBoardUseCase(localBoard)
    }

    fun deleteBoardItem(localBoardItem: LocalBoard.Item) = viewModelScope.launch {
        deleteBoardUseCase(localBoardItem)
    }

    fun setToken(token: String) {
        savedStateHandle["token"] = token
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

