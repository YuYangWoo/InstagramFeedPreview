package com.example.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.BoardChild
import com.example.usecase.FetchBoardChildItemUseCase
import com.example.usecase.ManageUserInformationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardDetailViewModel @Inject constructor(
    private val fetchBoardChildItemUseCase: FetchBoardChildItemUseCase,
    private val manageUserInformationUseCase: ManageUserInformationUseCase
) : ViewModel() {
    private val _boardDetailUiState = MutableStateFlow<BoardDetailUiState<BoardChild>>(BoardDetailUiState.Loading)
    val boardDetailUiState = _boardDetailUiState.asStateFlow()

    fun requestBoardChildItems(mediaId: String) = viewModelScope.launch {
        manageUserInformationUseCase.get().also { accessToken ->
            if (!accessToken.isNullOrBlank()) {
                _boardDetailUiState.value = BoardDetailUiState.Loading
                runCatching {
                    fetchBoardChildItemUseCase.invoke(mediaId, accessToken)
                }.onFailure {
                    _boardDetailUiState.value = BoardDetailUiState.Error("boardChild fetch Error!!")
                }.onSuccess { boardChild ->
                        _boardDetailUiState.value = boardChild?.let { BoardDetailUiState.Success(it) } ?: BoardDetailUiState.Error("boardChild is Null!!")
                    }
            } else {
                _boardDetailUiState.value = BoardDetailUiState.Error("accessToken is Error!!")
            }
        }
    }

}
sealed class BoardDetailUiState<out T> {
    object Loading : BoardDetailUiState<Nothing>()
    data class Success<T>(val data: T) : BoardDetailUiState<T>()
    data class Error(val message: String) : BoardDetailUiState<Nothing>()
}


