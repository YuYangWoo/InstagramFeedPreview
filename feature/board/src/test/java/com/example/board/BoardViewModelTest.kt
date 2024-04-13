package com.example.board

import com.example.board.viewmodel.BoardDetailUiState
import com.example.board.viewmodel.BoardUiState
import com.example.board.viewmodel.BoardViewModel
import com.example.model.BoardEntity
import com.example.model.BoardDetailEntity
import com.example.usecase.FetchBoardChildItemUseCase
import com.example.usecase.FetchInstagramBoardUseCase
import com.example.usecase.ManageUserInformationUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

class BoardViewModelTest : BehaviorSpec({
    Given("BoardViewModel 이 주어졌을 때") {
        val manageUserInformationUseCase = mockk<ManageUserInformationUseCase>()
        val fetchInstagramBoardUseCase = mockk<FetchInstagramBoardUseCase>()
        val fetchBoardChildItemUseCase = mockk<FetchBoardChildItemUseCase>()
        val boardViewModel = BoardViewModel(manageUserInformationUseCase, fetchInstagramBoardUseCase, fetchBoardChildItemUseCase)

        val fakeAccessToken = "fakeAccessToken"
        val boardEntity = BoardEntity(arrayListOf(BoardEntity.Item("id","caption", "mediaUrl")))

        beforeTest {
            Dispatchers.setMain(Dispatchers.Default)
        }
        afterTest {
            Dispatchers.resetMain()
        }

        When("requestBoardItem이 호출되고 결과가 성공일 때") {
            coEvery { manageUserInformationUseCase.get() } returns fakeAccessToken
            coEvery { fetchInstagramBoardUseCase.invoke(fakeAccessToken) } returns boardEntity

            boardViewModel.requestBoardItem()

            Then("UiState는 UiState.Success를 반환한다") {
                val boardUiState = boardViewModel.boardLocalUiState.value
                boardUiState shouldBe BoardUiState.Success(boardEntity)
            }
        }

        When("requestBoardItem이 호출되고 결과가 실패일 때") {
            coEvery { manageUserInformationUseCase.get() } returns fakeAccessToken
            coEvery { fetchInstagramBoardUseCase.invoke(fakeAccessToken) } throws Exception("board fetch Error!!")

            boardViewModel.requestBoardItem()

            Then("UiState는 UiState.Error를 반환한다") {
                val boardUiState = boardViewModel.boardLocalUiState.value
                boardUiState shouldBe BoardUiState.Error("board fetch Error!!")
            }
        }

        When("requestBoardItem이 호출되고 결과가 null일 때") {
            coEvery { manageUserInformationUseCase.get() } returns fakeAccessToken
            coEvery { fetchInstagramBoardUseCase.invoke(fakeAccessToken) } returns null

            boardViewModel.requestBoardItem()

            Then("UiState는 UiState.Error를 반환한다") {
                val boardUiState = boardViewModel.boardLocalUiState.value
                boardUiState shouldBe BoardUiState.Error("board is Null!!")
            }
        }
    }

    Given("BoardViewModel이 주어졌을 때") {
        val manageUserInformationUseCase = mockk<ManageUserInformationUseCase>()
        val fetchInstagramBoardUseCase = mockk<FetchInstagramBoardUseCase>()
        val fetchBoardChildItemUseCase = mockk<FetchBoardChildItemUseCase>()
        val boardViewModel = BoardViewModel(manageUserInformationUseCase, fetchInstagramBoardUseCase, fetchBoardChildItemUseCase)

        val fakeAccessToken = "fakeAccessToken"
        val fakeMediaId = "fakeMediaId"
        val boardDetailEntity = BoardDetailEntity(arrayListOf(BoardDetailEntity.Item("id","mediaUrl")))

        beforeTest {
            Dispatchers.setMain(Dispatchers.Default)
        }
        afterTest {
            Dispatchers.resetMain()
        }

        When("requestBoardChildItems를 호출하고 성공할 때") {
            coEvery { manageUserInformationUseCase.get() } returns fakeAccessToken
            coEvery { fetchBoardChildItemUseCase.invoke(fakeMediaId, fakeAccessToken) } returns boardDetailEntity

            boardViewModel.requestBoardDetailItem(fakeMediaId)

            Then("UiState는 UiState.Success를 반환한다") {
                val boardDetailUiState = boardViewModel.boardDetailUiState.value
                boardDetailUiState shouldBe BoardDetailUiState.Success(boardDetailEntity)
            }
        }

        When("requestBoardChildItems를 호출하고 실패할 때") {
            coEvery { manageUserInformationUseCase.get() } returns fakeAccessToken
            coEvery { fetchBoardChildItemUseCase.invoke(fakeMediaId, fakeAccessToken) } throws Exception("boardDetail fetch Error!!")

            boardViewModel.requestBoardDetailItem(fakeMediaId)

            Then("UiState는 UiState.Error를 반환한다") {
                val boardDetailUiState = boardViewModel.boardDetailUiState.value
                boardDetailUiState shouldBe BoardDetailUiState.Error("boardDetail fetch Error!!")
            }
        }

        When("requestBoardChildItems를 호출하고 null을 반환할 ") {
            coEvery { manageUserInformationUseCase.get() } returns fakeAccessToken
            coEvery { fetchBoardChildItemUseCase.invoke(fakeMediaId, fakeAccessToken) } returns null

            boardViewModel.requestBoardDetailItem(fakeMediaId)

            Then("UiState는 UiState.Error를 반환한다") {
                val boardDetailUiState = boardViewModel.boardDetailUiState.value
                boardDetailUiState shouldBe BoardDetailUiState.Error("boardDetail is Null!!")
            }
        }

        When("requestBoardChildItems를 호출하고 accessToken이 null일 때") {
            coEvery { manageUserInformationUseCase.get() } returns null

            boardViewModel.requestBoardDetailItem(fakeMediaId)
            Then("UiState는 UiState.Error를 반환한다") {
                val boardDetailUiState = boardViewModel.boardDetailUiState.value
                boardDetailUiState shouldBe BoardDetailUiState.Error("accessToken is Error!!")
            }
        }

        When("requestBoardChildItems를 호출하고 accessToken이 Blank일 때") {
            coEvery { manageUserInformationUseCase.get() } returns ""

            boardViewModel.requestBoardDetailItem(fakeMediaId)
            Then("UiState는 UiState.Error를 반환한다") {
                val boardDetailUiState = boardViewModel.boardDetailUiState.value
                boardDetailUiState shouldBe BoardDetailUiState.Error("accessToken is Error!!")
            }
        }

    }
})