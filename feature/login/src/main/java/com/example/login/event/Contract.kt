package com.example.login.event

import com.example.model.Login

sealed interface Contract {

    sealed interface Event {
        data class RequestAccessToken(val login: Login) : Event
        data class SaveUserAccessToken(val accessToken: String) : Event
    }

}