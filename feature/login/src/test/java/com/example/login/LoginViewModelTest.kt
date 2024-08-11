import androidx.lifecycle.SavedStateHandle
import com.example.login.LoginViewModel
import com.example.login.UiLogin
import com.example.login.event.Contract
import com.example.login.state.LoginUiState
import com.example.model.LongToken
import com.example.usecase.FetchInstagramTokenUseCase
import com.example.usecase.SaveUserAccessTokenUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.Test
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
    lateinit var fetchInstagramTokenUseCase: FetchInstagramTokenUseCase
    lateinit var saveUserAccessTokenUseCase: SaveUserAccessTokenUseCase
    lateinit var savedStateHandle: SavedStateHandle
    val longToken = LongToken("fake_access_token", "fake_token_type", "fake_expires_in")

    beforeTest {
        Dispatchers.setMain(testDispatcher)
        fetchInstagramTokenUseCase = mockk()
        saveUserAccessTokenUseCase = mockk(relaxed = true)
        savedStateHandle = SavedStateHandle()
    }

    afterTest {
        Dispatchers.resetMain()
    }

    given("LoginViewModel is initialized") {
       When("login code is empty") {
            Then("the state should be Idle") {
                savedStateHandle["login"] = UiLogin("", "", "", "", "")
                viewModel = LoginViewModel(fetchInstagramTokenUseCase, saveUserAccessTokenUseCase, savedStateHandle)

                viewModel.loginUiState.value shouldBe LoginUiState.Idle
            }
        }

       When("login code is provided") {
            Then("the state should be Success with the correct access token") {
                val mockAccessToken = "mock_access_token"
                savedStateHandle["login"] = UiLogin("code", "mock", "mock", "mock", "mock")

                coEvery { fetchInstagramTokenUseCase(any()) } returns flowOf(
                    longToken
                )

                viewModel = LoginViewModel(fetchInstagramTokenUseCase, saveUserAccessTokenUseCase, savedStateHandle)

                testDispatcher.scheduler.advanceUntilIdle()

                viewModel.loginUiState.value.shouldBeInstanceOf<LoginUiState.Success>()
                (viewModel.loginUiState.value as LoginUiState.Success).loginState.accessToken shouldBe mockAccessToken
            }
        }

       When("IOException is thrown during login") {
            Then("the state should be NetworkError") {
                savedStateHandle["login"] = UiLogin("code", "mock", "mock", "mock", "mock")

                coEvery { fetchInstagramTokenUseCase(any()) } returns flow {
                    throw IOException("Network Error")
                }

                viewModel = LoginViewModel(fetchInstagramTokenUseCase, saveUserAccessTokenUseCase, savedStateHandle)

                testDispatcher.scheduler.advanceUntilIdle()

                viewModel.loginUiState.value.shouldBeInstanceOf<LoginUiState.Error.ErrorState.NetworkError>()
            }
        }

       When("SaveUserAccessToken event is triggered") {
            Then("the access token should be saved") {
                val mockAccessToken = "mock_access_token"

                viewModel = LoginViewModel(fetchInstagramTokenUseCase, saveUserAccessTokenUseCase, savedStateHandle)

                viewModel.event(Contract.Event.SaveUserAccessToken(mockAccessToken))

                coVerify { saveUserAccessTokenUseCase(mockAccessToken) }
            }
        }

        When("OnUpdateLoginInfo event is triggered") {
            Then("the login info in SavedStateHandle should be updated") {
                val uiLogin = UiLogin("code", "mock", "mock", "mock", "mock")

                viewModel = LoginViewModel(fetchInstagramTokenUseCase, saveUserAccessTokenUseCase, savedStateHandle)

                viewModel.event(Contract.Event.OnUpdateLoginInfo(uiLogin))

                savedStateHandle.get<UiLogin>("login") shouldBe uiLogin
            }
        }
    }
})