package com.example.pitstop.vista

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pitstop.controlador.PitStopController

@Composable
fun PantallaListado(navController: NavController, controlador: PitStopController) {
    var busqueda by remember { mutableStateOf("") }
    val paradas = if (busqueda.isEmpty()) controlador.obtenerParadas()
    else controlador.buscarParadas(busqueda)

    Column(Modifier.padding(16.dp)) {
        Text(text = "Listado de Pit Stops", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            label = { Text("Buscar piloto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            itemsIndexed(paradas) { index, parada ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(Modifier.padding(8.dp)) {
                        Text("Piloto: ${parada.piloto.nombre}")
                        Text("Tiempo: ${parada.tiempoSegundos} s")
                        Text("Estado: ${parada.estado.tipoEstado}")
                        Button(
                            onClick = { controlador.eliminarParada(index) },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                        ) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }
    }
}
