package com.example.android.tumblrx2.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_preferences")
class UserPreferences(
    context: Context
) {
    private val applicationContext = context.applicationContext
    private val dataStore = context.dataStore

    /**
     * returns the saved authToken value using kotlin Coroutines and Flow
     */
    val authToken: Flow<String?>
        get() = dataStore.data.map{  preferences ->
            preferences[KEY_AUTH]
        }

    /**
     * Saves the [authToken] got from the backend to the created dataStore
     */
    suspend fun saveAuthToken(authToken: String){
        dataStore.edit{ preferences ->
            preferences[KEY_AUTH] = authToken
        }
    }

    /**
     * The key to which our authToken value is mapped
     */
    companion object{
        private val KEY_AUTH = stringPreferencesKey("key_auth")
    }
}