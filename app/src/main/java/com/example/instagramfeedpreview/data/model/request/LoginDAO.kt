package com.example.instagramfeedpreview.data.model.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginDAO(
    val clientId: String,
    val clientSecret: String,
    val grantType: String,
    val redirectUri: String,
    val code: String
) : Parcelable
