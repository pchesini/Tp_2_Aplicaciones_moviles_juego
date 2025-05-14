package com.example.tp_2_juego.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val PLAYER_NAME = stringPreferencesKey("player_name")
        val BEST_SCORE = intPreferencesKey("best_score")
        val CURRENT_SCORE = intPreferencesKey("current_score")
        val RANKING = stringPreferencesKey("ranking")

        // TypeToken para deserializar Map<String, Int>
        val RANKING_TYPE = object : TypeToken<Map<String, Int>>() {}.type
    }

    private val gson = Gson()

    // Guardar nombre del jugador actual
    suspend fun savePlayerName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[PLAYER_NAME] = name
        }
    }

    fun getPlayerName(): Flow<String?> {
        return context.dataStore.data.map { it[PLAYER_NAME] }
    }

    // Puntaje actual
    suspend fun saveCurrentScore(score: Int) {
        context.dataStore.edit { prefs ->
            prefs[CURRENT_SCORE] = score
        }
    }

    fun getCurrentScore(): Flow<Int> {
        return context.dataStore.data.map { it[CURRENT_SCORE] ?: 0 }
    }

    // Mejor puntaje individual
    suspend fun saveBestScore(score: Int) {
        context.dataStore.edit { prefs ->
            prefs[BEST_SCORE] = score
        }
    }

    fun getBestScore(): Flow<Int> {
        return context.dataStore.data.map { it[BEST_SCORE] ?: 0 }
    }

    // Guardar ranking general
    suspend fun saveRankingScore(playerName: String, score: Int) {
        context.dataStore.edit { prefs ->
            val currentJson = prefs[RANKING] ?: "{}"
            val currentRanking: MutableMap<String, Int> =
                gson.fromJson(currentJson, RANKING_TYPE) ?: mutableMapOf()

            // Actualiza solo si el nuevo score es mejor
            val existingScore = currentRanking[playerName] ?: 0
            if (score > existingScore) {
                currentRanking[playerName] = score
            }

            prefs[RANKING] = gson.toJson(currentRanking)
        }
    }

    // Obtener ranking como Flow
    fun getRanking(): Flow<Map<String, Int>> {
        return context.dataStore.data.map { prefs ->
            val json = prefs[RANKING] ?: "{}"
            gson.fromJson(json, RANKING_TYPE)
        }
    }
}
