package com.example.tp_2_juego.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun GameScreen(navController: NavController) {
    var userGuess by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Adiviná el número (entre 1 y 100)", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userGuess,
            onValueChange = { userGuess = it },
            label = { Text("Tu intento") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val guess = userGuess.toIntOrNull()
            val target = 42 // Temporal, luego se genera aleatoriamente

            feedback = when {
                guess == null -> "Por favor, ingresá un número válido"
                guess < target -> "Demasiado bajo"
                guess > target -> "Demasiado alto"
                else -> {
                    // Ir a ResultScreen si adivinó
                    navController.navigate("result")
                    "¡Correcto!"
                }
            }
        }) {
            Text("Intentar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(feedback)
    }
}
