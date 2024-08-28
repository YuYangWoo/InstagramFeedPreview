package com.example.board.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.board.state.BoardUiEvent
import com.example.usecase.FetchInstagramBoardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.example.board.state.BoardUiState
import com.example.board.state.NavigateBoardDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class BoardViewModel @Inject constructor(
    private val fetchInstagramBoardUseCase: FetchInstagramBoardUseCase,
    savedStateHandle: SavedStateHandle,
    ) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val boardPagingData = savedStateHandle.getStateFlow("accessToken", "").flatMapLatest {
        fetchInstagramBoardUseCase(it).map { boardPagingData ->
            boardPagingData.map { item ->
                BoardUiState(
                    id = item.id,
                    mediaUrl = item.mediaUrl.orEmpty(),
                    onClick = {
                        event(
                            BoardUiEvent.OnNavigateBoardDetail(
                                item.id,
                                item.mediaUrl.orEmpty()
                            )
                        )
                    }
                )
            }
        }
    }.cachedIn(viewModelScope)

    private var _navigateBoardDetailUiState = MutableStateFlow(NavigateBoardDetailUiState())
    val navigateBoardDetailUiState = _navigateBoardDetailUiState.asStateFlow()

    fun event(event: BoardUiEvent) {
        when (event) {
            is BoardUiEvent.OnNavigateBoardDetail -> {
                navigateBoardDetail(event.id, event.mediaUrl)
            }

            BoardUiEvent.OnClearNavigateBoardDetail -> {
                clearNavigateBoardDetail()
            }
        }
    }

    private fun navigateBoardDetail(id: String, mediaUrl: String) {
        _navigateBoardDetailUiState.update {
            it.copy(
                shouldNavigateBoardDetail = true,
                id = id,
                mediaUrl = mediaUrl
            )
        }
    }

    private fun clearNavigateBoardDetail() {
        _navigateBoardDetailUiState.update {
            it.copy(shouldNavigateBoardDetail = false)
        }
    }

    companion object {
        private const val TAG = "BoardViewModel"
    }

}
