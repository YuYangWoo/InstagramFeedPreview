package com.example.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.datasource.UserDataStoreSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDataStoreSourceImpl @Inject constructor() : UserDataStoreSource {
    @Inject
    lateinit var dataStore: DataStore<Preferences>

    private val ACCESS_TOKEN = stringPreferencesKey(KEY_ACCESS_TOKEN)

    override suspend fun saveUserAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            val userToken = preferences[ACCESS_TOKEN] ?: accessToken
            preferences[ACCESS_TOKEN] = userToken
        }
    }

    override fun getUserAccessToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN]
        }
    }
    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
    }
}
