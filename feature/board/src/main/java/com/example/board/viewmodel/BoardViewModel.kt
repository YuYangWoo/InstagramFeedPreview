package com.example.board.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.board.event.Contract
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
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.board.state.BoardUiState

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val fetchInstagramBoardUseCase: FetchInstagramBoardUseCase,
    private val fetchBoardDetailItemUseCase: FetchBoardDetailItemUseCase,
    private val insertBoardUseCase: InsertBoardUseCase,
    private val findBoardUseCase: FindBoardUseCase,
    private val updateBoardUseCase: UpdateBoardUseCase,
    private val deleteBoardUseCase: DeleteBoardUseCase,
    savedStateHandle: SavedStateHandle,
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingData = savedStateHandle.getStateFlow("accessToken", "").flatMapLatest {
        fetchInstagramBoardUseCase(it).map { pagingData ->
            pagingData.map { item ->
                BoardUiState(
                    id = item.id,
                    mediaUrl = item.mediaUrl.orEmpty(),
                    onClick = { event(Contract.Event.OnClickPagingItem(item.id, item.mediaUrl.orEmpty())) }
                )
            }
        }
    }.cachedIn(viewModelScope)

    private var _effect: MutableSharedFlow<Contract.Effect> = MutableSharedFlow(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<Contract.Effect> = _effect.asSharedFlow()

    private fun event(event: Contract.Event) {
        when (event) {
            is Contract.Event.OnClickPagingItem -> {
                navigateBoardDetailFragment(event.id, event.mediaUrl)
            }
        }
    }

    private fun navigateBoardDetailFragment(id: String, mediaUrl: String) {
        _effect.tryEmit(Contract.Effect.NavigateBoardDetailFragment(id, mediaUrl))
    }

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

    companion object {
        private const val TAG = "BoardViewModel"
    }

}

sealed class BoardLocalUiState<out T> {
    object Loading : BoardLocalUiState<Nothing>()
    data class Success<T>(val data: T) : BoardLocalUiState<T>()
    data class Error(val message: String) : BoardLocalUiState<Nothing>()
}

