package com.example.pitstop.vista

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pitstop.controlador.PitStopController

@Composable
fun PantallaResumen(navController: NavController, controlador: PitStopController) {
    val (masRapido, promedio, total) = controlador.obtenerResumen()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Resumen de Pit Stops", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Pit stop más rápido: $masRapido s")
        Text("Promedio de tiempos: $promedio s")
        Text("Total de paradas: $total")
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { navController.navigate("registro") }) {
            Text("Registrar Pit Stop")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { navController.navigate("listado") }) {
            Text("Ver Listado")
        }
    }
}
