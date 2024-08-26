package com.example.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.usecase.FetchUserAccessTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    fetchUserAccessTokenUseCase: FetchUserAccessTokenUseCase
) : ViewModel() {

    val mainUiState = fetchUserAccessTokenUseCase()
        .onStart {
            MainUiState.Loading
        }.map { accessToken ->
            if (accessToken.isEmpty()) {
                MainUiState.Success(isShowBoardFragment = false, accessToken = accessToken)
            } else {
                MainUiState.Success(isShowBoardFragment = true, accessToken = accessToken)
            }
        }.catch { throwable ->
            onException(throwable)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MainUiState.Loading
        )

    private fun onException(throwable: Throwable): MainUiState.Error.ErrorState {
        return if (throwable is IOException) {
            MainUiState.Error.ErrorState.NetworkError(
                "네트워크에 접속할 수 없습니다. 네트워크 연결상태 확인 후 다시 시도해 주세요.", throwable
            )
        } else {
            MainUiState.Error.ErrorState.DefaultError(
                "알 수 없는 오류입니다. 잠시 후 다시 시도해 주세요.", throwable
            )
        }
    }

}
