package com.example.pitstop.vista

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pitstop.controlador.PitStop_Controller
import com.example.pitstop.modelo.Parada

@Composable
fun ListaParadasScreen(controller: PitStop_Controller, onVolver: () -> Unit) {
    val paradas = remember { mutableStateListOf<Parada>() }

    LaunchedEffect(Unit) {
        paradas.clear()
        paradas.addAll(controller.obtenerParadas())
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Listado de Paradas", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))

        if (paradas.isEmpty()) {
            Text("No hay paradas registradas.")
        } else {
            LazyColumn {
                items(paradas.indices.toList()) { index ->
                    val parada = paradas[index]
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("Piloto: ${parada.piloto.nombre}")
                            Text("Escudería: ${parada.escuderia.nombre}")
                            Text("Tiempo: ${parada.tiempoSegundos} seg")
                            Text("Neumático: ${parada.neumatico.tipo} (${parada.neumatico.cantidad})")
                            Text("Estado: ${parada.estado.tipoEstado}")
                            parada.observacion?.let { Text("Observación: $it") }
                            Text("Mecánico: ${parada.mecanico.nombre}")
                            Text("Fecha: ${parada.fecha}")
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = {
                                controller.eliminarParada(index)
                                paradas.removeAt(index)
                            }, modifier = Modifier.fillMaxWidth()) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
            Text("Volver al menú")
        }
    }
}
