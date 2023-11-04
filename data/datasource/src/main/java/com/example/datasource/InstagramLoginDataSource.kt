package com.example.datasource

import com.example.dto.TokenDTO

/**
 * 이 DataSource와 DataSourceImpl을 나눈 이유는
 * data layer는 순수 kotlin 코드로 이루어져야한다고 생각을 했다
 * 그래서 Room 이라는 Android의 종속적인 library를 모르게 하기 위해 이렇게 한 것
 */
public interface InstagramLoginDataSource {
    public suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        grantType: String,
        redirectUri: String,
        code: String
    ): TokenDTO

}
