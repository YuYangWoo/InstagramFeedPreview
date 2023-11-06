package com.example.repository

import com.example.model.Board
import com.example.model.Login
import com.example.model.Token
import kotlinx.coroutines.flow.Flow

/**
 * 1. Domain UseCase에서 RepositoryImpl에 데이터를 요청한다.
 * 2. 뭐가 문제일까에 대한 해답
 *
 * 어떨 때 문제가 생기는가?
 * 테스트? -> viewModel 만 테스트하면 되자
 * 도메인은 데이터를 참조안함
 * 도메인은 아무것도 의존성이 없다.
 * repository 기능은 필요하다
 * 의존역전의 법칙을 사용?
 * 클린아키텍처 domain은 아무것도 의존성이 없다.
 *
 */
interface InstagramRepository {

    fun fetchToken(login: Login): Flow<Token>

    fun fetchBoardInformation(
        accessToken: String
    ): Flow<Board>
    
}

