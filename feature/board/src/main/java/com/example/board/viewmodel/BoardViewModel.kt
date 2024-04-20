package com.example.board.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.board.state.BoardDetailUiState
import com.example.model.BoardDetail
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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
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

    private val _boardDetailUiState = MutableStateFlow(BoardDetailUiState())
    val boardDetailUiState = _boardDetailUiState.asStateFlow()

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

    fun requestBoardDetailItem(id: String, mediaUrl: String?) = viewModelScope.launch {
        fetchBoardDetailItemUseCase(id)
            .onStart {
                _boardDetailUiState.update {
                    it.copy(
                        isLoading = true,
                        items = listOf()
                    )
                }
            }.catch {
                _boardDetailUiState.update {
                    it.copy(errorMessage = "boardDetail is Error!!")
                }
            }.collectLatest { boardDetail ->
                _boardDetailUiState.update {
                    it.copy(
                        isLoading = false,
                        items = boardDetail.items.apply {
                            if (this.size == 0 && id.isNotEmpty() && !mediaUrl.isNullOrEmpty())
                                this.add(BoardDetail.Item(id = id, mediaUrl = mediaUrl))
                        }.map { item ->
                            BoardDetailUiState.Item(item.id, item.mediaUrl)
                        })
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

