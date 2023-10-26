package com.example.main

import com.example.usecase.ManageUserInformationUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import java.lang.Exception

class MainViewModelTest : BehaviorSpec({

    Given("MainViewModel이 주어졌을 때") {
        val fakeToken = "fakeAccessToken"
        val manageUserInformationUseCase = mockk<ManageUserInformationUseCase>()
        val viewModel = MainViewModel(manageUserInformationUseCase)

        beforeTest {
            Dispatchers.setMain(Dispatchers.Default)
        }
        afterTest {
            Dispatchers.resetMain()
        }

        When("getUserAccessToken 호출 후 결과가 성공적일 때") {
            coEvery { manageUserInformationUseCase.get() } returns fakeToken
            runTest {
                viewModel.getUserAccessToken()
            }
            Then("결과는 UiState.Success 이여야 한다") {
                val uiState = viewModel.uiState.value
                uiState shouldBe UiState.Success(fakeToken)
            }
        }

        When("getUserAccessToken 호출 후 결과가 실패했을 때") {
            coEvery { manageUserInformationUseCase.get() } throws Exception("Fake error")
            runTest {
                viewModel.getUserAccessToken()
            }

            Then("결과는 UiState.Error 이여야 한다") {
                val uiState = viewModel.uiState.value
                uiState shouldBe UiState.Error("getUserAccessToken method is Fail!!")
            }
        }

        When("getUserAccessToken 호출 후 결과가 비어 있을 때") {
            coEvery { manageUserInformationUseCase.get() } returns ""
            runTest {
                viewModel.getUserAccessToken()
            }

            Then("결과는 UiState.Error 이여야 한다") {
                val uiState = viewModel.uiState.value
                uiState shouldBe UiState.Error("userToken is nullOrBlank!!")
            }
        }

        When("getUserAccessToken 호출 후 결과가 null일 때") {
            coEvery { manageUserInformationUseCase.get() } returns null
            runTest {
                viewModel.getUserAccessToken()
            }

            Then("결과는 UiState.Error 이여야 한다") {
                val uiState = viewModel.uiState.value
                uiState shouldBe UiState.Error("userToken is nullOrBlank!!")
            }
        }

    }
})