package com.example.login.event

import com.example.login.UiLogin
import com.example.model.Login

sealed interface Contract {

    sealed interface Event {
        data class SaveUserAccessToken(val accessToken: String) : Event
        data class OnUpdateLoginInfo(val login: UiLogin) : Event
    }

}