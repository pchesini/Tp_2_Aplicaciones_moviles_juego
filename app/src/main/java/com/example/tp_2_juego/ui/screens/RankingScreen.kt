package com.example.tp_2_juego.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tp_2_juego.data.UserPreferences
import kotlinx.coroutines.flow.first

@Composable
fun RankingScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = remember { UserPreferences(context) }
    var ranking by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }

    LaunchedEffect(Unit) {
        ranking = prefs.getRanking().first()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Ranking de Jugadores", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            val sortedRanking = ranking.toList().sortedByDescending { it.second }
            items(sortedRanking) { (name, score) ->
                val color = when (sortedRanking.indexOfFirst { it.first == name }) {
                    0 -> Color(0xFFD4AF37)
                    1 -> Color(0xFFC0C0C0)
                    2 -> Color(0xFFCD7F32)
                    else -> MaterialTheme.colorScheme.onBackground
                }

                Text(
                    "$name: $score puntos",
                    color = color,
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            navController.popBackStack()
        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Volver")
        }
    }
}
