package com.example.tp_2_juego.data

import android.content.Context
import androidx.datastore.preferences.core.edit
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
        val RANKING = stringPreferencesKey("ranking")
        val BEST_SCORES = stringPreferencesKey("best_scores")
        val TOTAL_SCORES = stringPreferencesKey("total_scores")
        val CURRENT_SCORES = stringPreferencesKey("current_scores")
    }

    private val gson = Gson()
    private val mapType = object : TypeToken<Map<String, Int>>() {}.type

    suspend fun savePlayerName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[PLAYER_NAME] = name
        }
    }

    fun getPlayerName(): Flow<String?> {
        return context.dataStore.data.map { it[PLAYER_NAME] }
    }

    // ✅ Lógica corregida: siempre se actualiza el ranking
    suspend fun saveScoreForPlayer(playerName: String, roundScore: Int, isWin: Boolean) {
        context.dataStore.edit { prefs ->
            val totalMap = gson.fromJson<Map<String, Int>>(prefs[TOTAL_SCORES] ?: "{}", mapType).toMutableMap()
            val bestMap = gson.fromJson<Map<String, Int>>(prefs[BEST_SCORES] ?: "{}", mapType).toMutableMap()
            val currentMap = gson.fromJson<Map<String, Int>>(prefs[CURRENT_SCORES] ?: "{}", mapType).toMutableMap()
            val rankingMap = gson.fromJson<Map<String, Int>>(prefs[RANKING] ?: "{}", mapType).toMutableMap()

            val totalAnterior = totalMap[playerName] ?: 0
            val nuevoTotal = if (isWin) totalAnterior + roundScore else 0

            totalMap[playerName] = nuevoTotal
            currentMap[playerName] = roundScore

            // Si superó el mejor puntaje anterior
            if ((bestMap[playerName] ?: 0) < roundScore) {
                bestMap[playerName] = roundScore
            }

            // ✅ Siempre se actualiza el ranking, incluso si es 0
            rankingMap[playerName] = nuevoTotal

            prefs[TOTAL_SCORES] = gson.toJson(totalMap)
            prefs[CURRENT_SCORES] = gson.toJson(currentMap)
            prefs[BEST_SCORES] = gson.toJson(bestMap)
            prefs[RANKING] = gson.toJson(rankingMap)
        }
    }

    fun getTotalScore(playerName: String): Flow<Int> {
        return context.dataStore.data.map {
            val map = gson.fromJson<Map<String, Int>>(it[TOTAL_SCORES] ?: "{}", mapType)
            map[playerName] ?: 0
        }
    }

    fun getBestScore(playerName: String): Flow<Int> {
        return context.dataStore.data.map {
            val map = gson.fromJson<Map<String, Int>>(it[BEST_SCORES] ?: "{}", mapType)
            map[playerName] ?: 0
        }
    }

    fun getCurrentScore(playerName: String): Flow<Int> {
        return context.dataStore.data.map {
            val map = gson.fromJson<Map<String, Int>>(it[CURRENT_SCORES] ?: "{}", mapType)
            map[playerName] ?: 0
        }
    }

    fun getRanking(): Flow<Map<String, Int>> {
        return context.dataStore.data.map {
            val json = it[RANKING] ?: "{}"
            gson.fromJson(json, mapType)
        }
    }
}
