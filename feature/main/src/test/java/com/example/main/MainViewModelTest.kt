package com.example.main

import com.example.testing.MainDispatcherRule
import com.example.usecase.FetchUserAccessTokenUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fetchUserAccessTokenUseCase: FetchUserAccessTokenUseCase = mockk(relaxed = true)

    private lateinit var mainViewModel: MainViewModel

    @Test
    fun `최초의 MainUiState는 Loading이여야 한다`() = runTest {
        mainViewModel = MainViewModel(fetchUserAccessTokenUseCase)

        assertEquals(MainUiState.Loading, mainViewModel.mainUiState.value)
    }

    @Test
    fun `저장된 AccessToken이 있으면 게시판을 보여준다`() = runTest {
        val mockAccessToken = "mockAccessToken"
        coEvery { fetchUserAccessTokenUseCase() } returns flowOf(mockAccessToken)

        mainViewModel = MainViewModel(fetchUserAccessTokenUseCase)

        val job = launch(StandardTestDispatcher()) {
            mainViewModel.mainUiState.collect()
        }

        assertEquals(MainUiState.Loading, mainViewModel.mainUiState.value)

        advanceUntilIdle()

        assertEquals(MainUiState.Success(isShowBoardFragment = true, accessToken = mockAccessToken), mainViewModel.mainUiState.value)

        job.cancel()
    }

    @Test
    fun `저장된 AccessToken이 없으면 게시판을 보여주지 않는다`() = runTest {
        val mockAccessToken = ""
        coEvery { fetchUserAccessTokenUseCase() } returns flowOf(mockAccessToken)

        mainViewModel = MainViewModel(fetchUserAccessTokenUseCase)

        val job = launch(StandardTestDispatcher()) {
            mainViewModel.mainUiState.collect()
        }

        assertEquals(MainUiState.Loading, mainViewModel.mainUiState.value)

        advanceUntilIdle()

        assertEquals(MainUiState.Success(isShowBoardFragment = false, accessToken = mockAccessToken), mainViewModel.mainUiState.value)

        job.cancel()
    }

    @Test
    fun `네트워크 에러가 발생하면 ErrorState는 NetworkError이다`() = runTest {
        val mockException = IOException("네트워크 오류")
        coEvery { fetchUserAccessTokenUseCase() } returns flow {
            throw mockException
        }

        mainViewModel = MainViewModel(fetchUserAccessTokenUseCase)

        val job = launch(StandardTestDispatcher()) {
            mainViewModel.mainUiState.collect()
        }

        assertEquals(MainUiState.Loading, mainViewModel.mainUiState.value)

        advanceUntilIdle()

        assertEquals(
            MainUiState.Error.ErrorState.NetworkError(
                message = "네트워크에 접속할 수 없습니다. 네트워크 연결상태 확인 후 다시 시도해 주세요.",
                cause = mockException
            ),
            (mainViewModel.mainUiState.value as MainUiState.Error).errorState
        )

        job.cancel()
    }
}