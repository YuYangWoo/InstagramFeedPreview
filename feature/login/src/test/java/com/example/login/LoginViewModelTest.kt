package com.example.login

import com.example.model.Login
import com.example.model.ShortTokenEntity
import com.example.usecase.FetchInstagramTokenUseCase
import com.example.usecase.ManageUserInformationUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

class LoginViewModelTest : BehaviorSpec({

    Given("InstagramViewModel 이 주어졌을 때") {
        val fetchInstagramTokenUseCase = mockk<FetchInstagramTokenUseCase>()
        val manageUserInformationUseCase = mockk<ManageUserInformationUseCase>()
        val loginViewModel = LoginViewModel(fetchInstagramTokenUseCase, manageUserInformationUseCase)

        val login = Login("username", "password", "grantType", "redirectUri", "code")
        val shortTokenEntity = ShortTokenEntity("fakeAccessToken", "fakeUserId")

        beforeTest {
            Dispatchers.setMain(Dispatchers.Default)
        }
        afterTest {
            Dispatchers.resetMain()
        }

        coEvery { manageUserInformationUseCase.save(any()) } just Runs

        When("requestAccessToken 호출 후 결과가 성공적일 때") {
            coEvery { fetchInstagramTokenUseCase.invoke(login) } returns shortTokenEntity
            loginViewModel.requestAccessToken(login)

            Then("UiState 가 Success로 되어야 한다") {
                val uiState = loginViewModel.uiState.value
                uiState shouldBe UiState.Success(shortTokenEntity)
            }
        }

        When("requestAccessToken 호출 후 결과가 실패했을 때") {
            coEvery { fetchInstagramTokenUseCase.invoke(login)} throws Exception("token fetch Error!!")
            loginViewModel.requestAccessToken(login)

            Then("UiState 가 UiState.Error 이여야 한다") {
                val uiState = loginViewModel.uiState.value
                uiState shouldBe UiState.Error("token fetch Error!!")
            }
        }

        When("requestAccessToken 호출 후 null을 반환할 때") {
            coEvery { fetchInstagramTokenUseCase.invoke(login) } returns null
            loginViewModel.requestAccessToken(login)

            Then("UiState 가 UiState.Error 이여야 한다") {
                val uiState = loginViewModel.uiState.value
                uiState shouldBe UiState.Error("token is Null!!")
            }
        }

    }
})