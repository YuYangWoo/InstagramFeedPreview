package com.example.instagramfeedpreview.core.database

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataStore @Inject constructor() {
    @Inject
    lateinit var dataStore: DataStore<Preferences>
    private val ACCESS_TOKEN = stringPreferencesKey("access_token")

    suspend fun saveUserAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            val userToken = preferences[ACCESS_TOKEN] ?: accessToken
            preferences[ACCESS_TOKEN] = userToken
        }
    }

    fun getUserAccessToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]
        }
    }

}
