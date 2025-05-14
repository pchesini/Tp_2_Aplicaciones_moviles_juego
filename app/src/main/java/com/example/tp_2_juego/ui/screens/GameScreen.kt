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
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun GameScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    val scope = rememberCoroutineScope()

    var currentNumber by remember { mutableStateOf(Random.nextInt(1, 6)) }
    var input by remember { mutableStateOf("") }
    var score by remember { mutableStateOf(0) }
    var bestScore by remember { mutableStateOf(0) }
    var errorCount by remember { mutableStateOf(0) }

    // Cargar mejor puntaje desde DataStore
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
        Text("Puntaje: $score", style = MaterialTheme.typography.headlineMedium)
        Text("Mejor Puntaje: $bestScore", style = MaterialTheme.typography.bodyLarge)

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
                score += 10
                errorCount = 0

                if (score > bestScore) {
                    bestScore = score
                    scope.launch {
                        prefs.saveBestScore(bestScore)
                    }
                }

                scope.launch {
                    prefs.saveCurrentScore(score)
                    navController.navigate("result")
                }
            } else {
                errorCount++
                if (errorCount >= 5) {
                    Toast.makeText(context, "Perdiste. Puntaje reiniciado.", Toast.LENGTH_SHORT).show()
                    score = 0
                    errorCount = 0

                    scope.launch {
                        prefs.saveCurrentScore(score)
                        navController.navigate("result")
                    }
                } else {
                    Toast.makeText(context, "Incorrecto. Intentos fallidos: $errorCount", Toast.LENGTH_SHORT).show()
                }
            }

            currentNumber = Random.nextInt(1, 6)
            input = ""
        }) {
            Text("Adivinar")
        }
    }
}