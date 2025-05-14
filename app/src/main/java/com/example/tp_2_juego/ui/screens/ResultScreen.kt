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
    val scope = rememberCoroutineScope()

    var playerName by remember { mutableStateOf("") }
    var currentScore by remember { mutableStateOf(0) }
    var bestScore by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        prefs.getPlayerName().collect {
            playerName = it ?: "Jugador"
        }
    }

    LaunchedEffect(Unit) {
        prefs.getCurrentScore().collect {
            currentScore = it
        }
    }

    LaunchedEffect(Unit) {
        prefs.getBestScore().collect {
            bestScore = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("¡Juego Finalizado!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Jugador: $playerName", style = MaterialTheme.typography.bodyLarge)
        Text("Tu puntaje fue: $currentScore", style = MaterialTheme.typography.bodyLarge)
        Text("Mejor puntaje: $bestScore", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(24.dp))

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