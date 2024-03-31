package com.example.usecase

import com.example.model.LoginEntity
import com.example.model.LongTokenEntity
import com.example.repository.InstagramRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchInstagramTokenUseCase @Inject constructor(
    private val instagramRepository: InstagramRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(loginEntity: LoginEntity): Flow<LongTokenEntity> {
        return instagramRepository.fetchShortToken(loginEntity)
            .flatMapLatest { shortToken ->
                instagramRepository.fetchLongToken(
                    "ig_exchange_token",
                    loginEntity.clientSecret,
                    shortToken.accessToken,
                )
            }

    }
}
