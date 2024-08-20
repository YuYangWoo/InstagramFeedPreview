package com.example.login

import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import androidx.lifecycle.SavedStateHandle
import com.example.login.LoginViewModel
import com.example.login.event.Contract
import com.example.login.state.LoginUiState
import com.example.model.Login
import com.example.model.LongToken
import com.example.testing.MainDispatcherRule
import com.example.usecase.FetchInstagramTokenUseCase
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class LoginViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val fetchInstagramTokenUseCase: FetchInstagramTokenUseCase = mockk()
    private val savedStateHandle = SavedStateHandle()
    private lateinit var loginViewModel: LoginViewModel

    @Test
    fun `최초의 LoginUiState는 Idle이여야 한다`() = runTest {
        loginViewModel = LoginViewModel(fetchInstagramTokenUseCase, savedStateHandle)

        loginViewModel.loginUiState.value shouldBe LoginUiState.Idle
    }

    @Test
    fun `Login 이벤트가 전달되면 성공이고 accessToken을 확인할 수 있다`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher())
        loginViewModel = LoginViewModel(fetchInstagramTokenUseCase, savedStateHandle)

        val mockLongToken = LongToken("mockAccessToken", "mockTokenType", "mockExpiresIn")
        coEvery { fetchInstagramTokenUseCase(any()) } returns mockLongToken

        loginViewModel.event(Contract.Event.OnUpdateLoginInfo(UiLogin("mockCode", "mockClientId", "mockClientSecret", "mockRedirectUri", "mockGrantType")))
        advanceUntilIdle()
        assertEquals(
            LoginUiState.Success(LoginUiState.Success.LoginState(true, mockLongToken.accessToken)),
            loginViewModel.loginUiState.value
        )


    }
}