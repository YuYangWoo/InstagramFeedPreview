package com.example.login

import android.os.Parcelable
import com.example.model.Login
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiLogin(
    val clientId: String,
    val clientSecret: String,
    val grantType: String,
    val redirectUri: String,
    val code: String
) : Parcelable

fun UiLogin.toLogin(): Login {
    return Login(
        clientId = clientId,
        clientSecret = clientSecret,
        grantType = grantType,
        redirectUri = redirectUri,
        code = code
    )
}