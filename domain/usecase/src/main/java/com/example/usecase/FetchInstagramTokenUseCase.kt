package com.example.usecase

import com.example.model.LoginEntity
import com.example.model.LongTokenEntity
import com.example.repository.InstagramRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FetchInstagramTokenUseCase @Inject constructor(
    private val instagramRepository: InstagramRepository
) {
    operator fun invoke(loginEntity: LoginEntity): Flow<LongTokenEntity> {
        return instagramRepository.fetchShortToken(loginEntity)
            .map {
                instagramRepository.fetchLongToken(
                    "ig_exchange_token",
                    loginEntity.clientSecret,
                    it.accessToken,
                )
            }
            .flatMapLatest {
                it
            }
    }
}
