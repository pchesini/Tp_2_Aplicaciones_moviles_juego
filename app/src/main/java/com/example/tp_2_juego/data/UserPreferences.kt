package com.example.tp_2_juego.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val PLAYER_NAME = stringPreferencesKey("player_name")
        val BEST_SCORE = intPreferencesKey("best_score")
        val CURRENT_SCORE = intPreferencesKey("current_score") // ✅ AÑADIDO
    }

    suspend fun savePlayerName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[PLAYER_NAME] = name
        }
    }

    fun getPlayerName(): Flow<String?> {
        return context.dataStore.data.map { it[PLAYER_NAME] }
    }

    suspend fun saveBestScore(score: Int) {
        context.dataStore.edit { prefs ->
            prefs[BEST_SCORE] = score
        }
    }

    fun getBestScore(): Flow<Int> {
        return context.dataStore.data.map { it[BEST_SCORE] ?: 0 }
    }


    suspend fun saveCurrentScore(score: Int) {
        context.dataStore.edit { prefs ->
            prefs[CURRENT_SCORE] = score
        }
    }

    fun getCurrentScore(): Flow<Int> {
        return context.dataStore.data.map { it[CURRENT_SCORE] ?: 0 }
    }
}
