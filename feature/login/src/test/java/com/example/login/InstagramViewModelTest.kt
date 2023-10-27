package com.example.login

import com.example.model.Login
import com.example.model.Token
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

class InstagramViewModelTest : BehaviorSpec({

    Given("InstagramViewModel 이 주어졌을 때") {
        val fetchInstagramTokenUseCase = mockk<FetchInstagramTokenUseCase>()
        val manageUserInformationUseCase = mockk<ManageUserInformationUseCase>()
        val instagramViewModel = InstagramViewModel(fetchInstagramTokenUseCase, manageUserInformationUseCase)

        val login = Login("username", "password", "grantType", "redirectUri", "code")
        val token = Token("fakeAccessToken", "fakeUserId")

        beforeTest {
            Dispatchers.setMain(Dispatchers.Default)
        }
        afterTest {
            Dispatchers.resetMain()
        }

        coEvery { manageUserInformationUseCase.save(any()) } just Runs

        When("requestAccessToken 호출 후 결과가 성공적일 때") {
            coEvery { fetchInstagramTokenUseCase.invoke(login) } returns token
            instagramViewModel.requestAccessToken(login)

            Then("UiState 가 Success로 되어야 한다") {
                val uiState = instagramViewModel.uiState.value
                uiState shouldBe UiState.Success(token)
            }
        }

        When("requestAccessToken 호출 후 결과가 실패했을 때") {
            coEvery { fetchInstagramTokenUseCase.invoke(login)} throws Exception("token fetch Error!!")
            instagramViewModel.requestAccessToken(login)

            Then("UiState 가 UiState.Error 이여야 한다") {
                val uiState = instagramViewModel.uiState.value
                uiState shouldBe UiState.Error("token fetch Error!!")
            }
        }

        When("requestAccessToken 호출 후 null을 반환할 때") {
            coEvery { fetchInstagramTokenUseCase.invoke(login) } returns null
            instagramViewModel.requestAccessToken(login)

            Then("UiState 가 UiState.Error 이여야 한다") {
                val uiState = instagramViewModel.uiState.value
                uiState shouldBe UiState.Error("token is Null!!")
            }
        }

    }
})