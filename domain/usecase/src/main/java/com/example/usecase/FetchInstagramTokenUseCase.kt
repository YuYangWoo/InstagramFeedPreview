package com.example.usecase

import com.example.model.Login
import com.example.model.LongToken
import com.example.repository.InstagramRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchInstagramTokenUseCase @Inject constructor(
    private val instagramRepository: InstagramRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(login: Login): LongToken {
        return instagramRepository.fetchShortToken(login)
            .flatMapLatest { shortToken ->
                instagramRepository.fetchLongToken(
                    "ig_exchange_token",
                    login.clientSecret,
                    shortToken.accessToken,
                )
            }.first()
    }
}
