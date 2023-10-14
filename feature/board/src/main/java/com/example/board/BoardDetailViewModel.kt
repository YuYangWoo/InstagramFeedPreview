package com.example.board

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Board
import com.example.model.BoardChild
import com.example.usecase.FetchBoardChildItemUseCase
import com.example.usecase.ManageUserInformationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardDetailViewModel @Inject constructor(
    private val fetchBoardChildItemUseCase: FetchBoardChildItemUseCase,
    private val manageUserInformationUseCase: ManageUserInformationUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<BoardChild>>(UiState.Empty)
    val uiState: StateFlow<UiState<BoardChild>> = _uiState.asStateFlow()
    fun requestBoardChildItems(mediaId: String) = viewModelScope.launch {
        manageUserInformationUseCase.get().also { accessToken ->
            if (!accessToken.isNullOrBlank()) {
                _uiState.value = UiState.Loading
                runCatching {
                    fetchBoardChildItemUseCase.invoke(mediaId, accessToken)
                }.onFailure {
                    _uiState.value = UiState.Error("boardChild fetch Error!!")
                }.onSuccess { boardChild ->
                        _uiState.value = boardChild?.let { UiState.Success(it) } ?: UiState.Error("boardChild is Null!!")
                    }
            } else {
                _uiState.value = UiState.Error("accessToken is Error!!")
            }
        }
    }

}

