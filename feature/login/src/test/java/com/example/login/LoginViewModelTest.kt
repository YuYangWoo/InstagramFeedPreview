import androidx.lifecycle.SavedStateHandle
import com.example.login.LoginViewModel
import com.example.login.UiLogin
import com.example.login.event.Contract
import com.example.login.state.LoginUiState
import com.example.model.LongToken
import com.example.usecase.FetchInstagramTokenUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.Test
import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import java.io.IOException
import kotlin.math.truncate

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest : BehaviorSpec({

    val testDispatcher = StandardTestDispatcher()

    lateinit var viewModel: LoginViewModel
    val fetchInstagramTokenUseCase: FetchInstagramTokenUseCase = mockk()
    val savedStateHandle = SavedStateHandle()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    Given("LoginViewModel이 주어지고") {

        When("로그인하는 동안 오류가 발생할 때") {
            viewModel = LoginViewModel(fetchInstagramTokenUseCase, savedStateHandle)

            savedStateHandle["login"] = UiLogin("code", "mock", "mock", "mock", "mock")
            coEvery { fetchInstagramTokenUseCase(any()) } throws Exception("network error")
            Then("UiState는 Error를 반환한다.") {
                testDispatcher.scheduler.advanceUntilIdle()

                viewModel.loginUiState.value.shouldBeInstanceOf<LoginUiState.Error>()
            }
        }

       When("UiLogin의 code가 비어있을 때") {
           viewModel = LoginViewModel(fetchInstagramTokenUseCase, savedStateHandle)

           savedStateHandle["login"] = UiLogin("", "", "", "", "")

            Then("UiState는 Idle이어야한다.") {
                viewModel.loginUiState.value shouldBe LoginUiState.Idle
            }
        }

        When("UiLogin의 code가 비어있지 않을 때") {
            viewModel = LoginViewModel(fetchInstagramTokenUseCase, savedStateHandle)

            val longToken = LongToken("fake_access_token", "fake_token_type", "fake_expires_in")
            savedStateHandle["login"] = UiLogin("code", "mock", "mock", "mock", "mock")
            coEvery { fetchInstagramTokenUseCase(any()) } returns longToken

            Then("UiState는 성공이고 accessToken이 일치해야한다.") {
                testDispatcher.scheduler.advanceUntilIdle()

                viewModel.loginUiState.value shouldBe LoginUiState.Success(LoginUiState.Success.LoginState(true, longToken.accessToken))
            }
        }

        When("ViewModel 이벤트 OnUpdateLoginInfo가 발생할 때") {
            viewModel = LoginViewModel(fetchInstagramTokenUseCase, savedStateHandle)

            val longToken = LongToken("fake_access_token", "fake_token_type", "fake_expires_in")
            savedStateHandle["login"] = UiLogin("code", "mock", "mock", "mock", "mock")
            coEvery { fetchInstagramTokenUseCase(any()) } returns longToken
            Then("loginUiState는 발행되어야한다.") {
                viewModel.event(Contract.Event.OnUpdateLoginInfo(UiLogin("code", "mock", "mock", "mock", "mock")))
                testDispatcher.scheduler.advanceUntilIdle()

                viewModel.loginUiState.value shouldBe LoginUiState.Success(LoginUiState.Success.LoginState(true, longToken.accessToken))
            }
        }

    }
})