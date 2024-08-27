package com.example.board.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.board.state.BoardDetailUiState
import com.example.usecase.FetchBoardDetailItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class BoardDetailViewModel @Inject constructor(
    private val fetchBoardDetailItemUseCase: FetchBoardDetailItemUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val boardDetailUiState = combine(
        savedStateHandle.getStateFlow("id", ""),
        savedStateHandle.getStateFlow("mediaUrl", ""),
        ::Pair
    ).flatMapLatest { (id, mediaUrl) ->
        fetchBoardDetailItemUseCase(id).map { boardDetail ->
            val items =
                if (boardDetail.items.isEmpty() && id.isNotEmpty() && mediaUrl.isNotEmpty()) {
                    listOf(BoardDetailUiState.Item(id, mediaUrl))
                } else {
                    boardDetail.items.map { item ->
                        BoardDetailUiState.Item(item.id, item.mediaUrl)
                    }
                }
            BoardDetailUiState.Success(items)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = BoardDetailUiState.Loading
    )

}