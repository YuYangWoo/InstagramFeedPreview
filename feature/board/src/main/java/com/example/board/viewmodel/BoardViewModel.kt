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
@OptIn(ExperimentalCoroutinesApi::class)
class BoardViewModel @Inject constructor(
    private val fetchInstagramBoardUseCase: FetchInstagramBoardUseCase,
    savedStateHandle: SavedStateHandle,
    ) : ViewModel() {

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

    companion object {
        private const val TAG = "BoardViewModel"
    }

}
