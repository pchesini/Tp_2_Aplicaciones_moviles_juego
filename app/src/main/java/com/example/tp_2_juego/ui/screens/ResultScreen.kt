package com.example.tp_2_juego.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tp_2_juego.data.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun ResultScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }

    var playerName by remember { mutableStateOf("Jugador") }
    var bestScore by remember { mutableStateOf(0) }
    var totalScore by remember { mutableStateOf(0) }
    var roundScore by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        playerName = prefs.getPlayerName().first() ?: "Jugador"
        bestScore = prefs.getBestScore(playerName).first()
        totalScore = prefs.getTotalScore(playerName).first()
        roundScore = prefs.getCurrentScore(playerName).first()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Resultado", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Jugador: $playerName", style = MaterialTheme.typography.bodyLarge)
        Text("Puntaje de la Ronda: $roundScore", style = MaterialTheme.typography.bodyLarge)
        Text("Mejor Puntaje: $bestScore", style = MaterialTheme.typography.bodyLarge)
        Text("Puntaje Total: $totalScore", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            navController.navigate("start") {
                popUpTo("start") { inclusive = true }
            }
        }) {
            Text("Volver a Jugar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("ranking")
        }) {
            Text("Ver Ranking")
        }
    }
}
