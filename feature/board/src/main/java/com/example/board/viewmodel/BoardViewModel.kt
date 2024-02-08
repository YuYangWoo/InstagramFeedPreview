package com.example.board.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.model.BoardDetailEntity
import com.example.model.LocalBoardEntity
import com.example.usecase.DeleteBoardUseCase
import com.example.usecase.FetchBoardChildItemUseCase
import com.example.usecase.FetchInstagramBoardUseCase
import com.example.usecase.FindBoardUseCase
import com.example.usecase.InsertBoardUseCase
import com.example.usecase.ManageUserInformationUseCase
import com.example.usecase.UpdateBoardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
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
    private val deleteBoardUseCase: DeleteBoardUseCase,
    private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {

    private val _boardLocalUiState = MutableStateFlow<BoardLocalUiState<List<LocalBoardEntity.Item>>>(BoardLocalUiState.Loading)
    val boardLocalUiState = _boardLocalUiState.asStateFlow()

    private val _boardDetailEntityUiState = MutableStateFlow<BoardDetailUiState<BoardDetailEntity>>(BoardDetailUiState.Loading)
    val boardDetailUiState = _boardDetailEntityUiState.asStateFlow()

    private val token = savedStateHandle.getStateFlow("token", "")

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingData = token.mapLatest {
        fetchInstagramBoardUseCase(it)
    }.flatMapLatest {
        it
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
        _boardDetailEntityUiState.value = BoardDetailUiState.Loading

        manageUserInformationUseCase.get().map { accessToken ->
            if (!accessToken.isNullOrBlank()) {
                fetchBoardChildItemUseCase(id, accessToken).catch {
                    _boardDetailEntityUiState.value = BoardDetailUiState.Error("boardDetail is Null!!")
                }.collectLatest { boardDetail ->
                    _boardDetailEntityUiState.value = boardDetail.let {
                        if (it.items.size == 0 && !id.isNullOrEmpty() && !mediaUrl.isNullOrEmpty()) {
                            it.items.add(BoardDetailEntity.Item(id = id, mediaUrl = mediaUrl))
                        }
                        BoardDetailUiState.Success(it)
                    }
                }
            } else {
                _boardDetailEntityUiState.value = BoardDetailUiState.Error("accessToken is Error!!")
            }
        }
    }

    fun insertBoardItem(localBoardEntity: LocalBoardEntity) = viewModelScope.launch {
        insertBoardUseCase(localBoardEntity)
    }

    fun insertAdditionalBoardItem(localBoardEntity: LocalBoardEntity) = viewModelScope.launch {
        insertBoardUseCase.invokeAdditional(localBoardEntity)
    }

    fun updateBoardItem(localBoardEntity: LocalBoardEntity) = viewModelScope.launch {
        updateBoardUseCase(localBoardEntity)
    }

    fun deleteBoardItem(localBoardEntityItem: LocalBoardEntity.Item) = viewModelScope.launch {
        deleteBoardUseCase(localBoardEntityItem)
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

sealed class BoardDetailUiState<out T> {
    object Loading : BoardDetailUiState<Nothing>()
    data class Success<T>(val data: T) : BoardDetailUiState<T>()
    data class Error(val message: String) : BoardDetailUiState<Nothing>()
}
