package com.example.instagramfeedpreview.domain.usecase

import com.example.instagramfeedpreview.data.model.request.LoginDAO
import com.example.instagramfeedpreview.data.model.response.BoardDTO
import com.example.instagramfeedpreview.data.model.response.TokenDTO
import com.example.instagramfeedpreview.data.repository.instagramRepository.InstagramRepositoryImpl
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstagramBoardFetchUseCase @Inject constructor(private val instagramRepositoryImpl: InstagramRepositoryImpl) {
    suspend operator fun invoke(loginDAO: LoginDAO): BoardDTO? {
        var boardDTO: BoardDTO? = null

        instagramRepositoryImpl.fetchToken(loginDAO).collectLatest { value: TokenDTO ->
           instagramRepositoryImpl.fetchBoardInformation(value.accessToken).collectLatest { value: BoardDTO ->
               boardDTO = value
           }
        }
        return boardDTO
    }
}
