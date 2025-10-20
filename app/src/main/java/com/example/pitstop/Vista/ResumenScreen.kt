package com.example.pitstop.vista

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pitstop.controlador.PitStop_Controller

@Composable
fun ResumenScreen(controller: PitStop_Controller, onVolver: () -> Unit) {
    val resumen = remember { controller.obtenerResumen() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Resumen General", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))
        Text(resumen)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
            Text("Volver al menú")
        }
    }
}
