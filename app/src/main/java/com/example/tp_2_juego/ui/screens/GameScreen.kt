package com.example.tp_2_juego.ui.screens

import android.widget.Toast
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
import kotlin.random.Random

@Composable
fun GameScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    val scope = rememberCoroutineScope()

    var playerName by remember { mutableStateOf("Jugador") }
    var score by remember { mutableStateOf(0) }
    var bestScore by remember { mutableStateOf(0) }
    var totalScore by remember { mutableStateOf(0) }
    var errorCount by remember { mutableStateOf(0) }
    var input by remember { mutableStateOf("") }
    var acertadoAlMenosUnaVez by remember { mutableStateOf(false) }

    var currentNumber by remember { mutableStateOf(0) }

    // ✅ Solo se genera una vez al inicio del juego
    LaunchedEffect(Unit) {
        currentNumber = Random.nextInt(1, 6)
        playerName = prefs.getPlayerName().first() ?: "Jugador"
        prefs.getBestScore(playerName).collect { bestScore = it }
        prefs.getTotalScore(playerName).collect { totalScore = it }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Jugador: $playerName", style = MaterialTheme.typography.headlineMedium)
        Text("Puntaje ronda: $score", style = MaterialTheme.typography.headlineMedium)
        Text("Mejor Puntaje: $bestScore", style = MaterialTheme.typography.bodyLarge)
        Text("Puntaje Total: $totalScore", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Adiviná un número del 1 al 5") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val guess = input.toIntOrNull()
            if (guess == null || guess !in 1..5) {
                Toast.makeText(context, "Ingresá un número válido entre 1 y 5", Toast.LENGTH_SHORT).show()
                return@Button
            }

            if (guess == currentNumber) {
                val puntos = when (errorCount) {
                    0 -> 10
                    1 -> 8
                    2 -> 6
                    3 -> 4
                    4 -> 2
                    else -> 0
                }

                score += puntos
                acertadoAlMenosUnaVez = true
                errorCount = 0

                if (score > bestScore) bestScore = score

                scope.launch {
                    prefs.saveScoreForPlayer(playerName, score, isWin = true)
                    navController.navigate("result")
                }

            } else {
                errorCount++
                if (errorCount >= 5) {
                    Toast.makeText(context, "Perdiste. Puntaje reiniciado.", Toast.LENGTH_SHORT).show()
                    score = 0
                    errorCount = 0

                    scope.launch {
                        prefs.saveScoreForPlayer(playerName, score, isWin = acertadoAlMenosUnaVez)
                        navController.navigate("result")
                    }

                    acertadoAlMenosUnaVez = false
                } else {
                    Toast.makeText(context, "Incorrecto. Intentos fallidos: $errorCount", Toast.LENGTH_SHORT).show()
                }
            }

            input = ""
        }) {
            Text("Adivinar")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            navController.navigate("ranking")
        }) {
            Text("Ver Ranking")
        }
    }
}
