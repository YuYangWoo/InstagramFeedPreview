package com.example.login

import androidx.lifecycle.SavedStateHandle
import com.example.login.state.LoginUiEvent
import com.example.login.state.LoginUiState
import com.example.model.LongToken
import com.example.testing.MainDispatcherRule
import com.example.usecase.FetchInstagramTokenUseCase
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fetchInstagramTokenUseCase: FetchInstagramTokenUseCase = mockk(relaxed = true)
    private val savedStateHandle = SavedStateHandle()
    private lateinit var loginViewModel: LoginViewModel

    @Test
    fun `최초의 LoginUiState는 Idle이여야 한다`() = runTest {
        loginViewModel = LoginViewModel(fetchInstagramTokenUseCase, savedStateHandle)

        loginViewModel.loginUiState.value shouldBe LoginUiState.Idle
    }

    @Test
    fun `Login 이벤트가 전달되면 성공이고 accessToken을 확인할 수 있다`() = runTest {
        loginViewModel = LoginViewModel(fetchInstagramTokenUseCase, savedStateHandle)

        val mockLongToken = LongToken("mockAccessToken", "mockTokenType", "mockExpiresIn")
        coEvery { fetchInstagramTokenUseCase(any()) } returns mockLongToken

        val uiLogin = UiLogin(
            "mockCode",
            "mockClientId",
            "mockClientSecret",
            "mockRedirectUri",
            "mockGrantType"
        )
        loginViewModel.event(LoginUiEvent.OnUpdateLoginInfo(uiLogin))

        val job = launch(UnconfinedTestDispatcher()) {
            loginViewModel.loginUiState.collect()
        }

        assertEquals(
            LoginUiState.Success(LoginUiState.Success.LoginState(true, mockLongToken.accessToken)),
            loginViewModel.loginUiState.value
        )

        job.cancel()
    }
}