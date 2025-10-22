package com.example.pitstop.vista

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.example.pitstop.controlador.PitStop_Controller
import com.example.pitstop.modelo.Parada

@Composable
fun ListaParadasScreen(controller: PitStop_Controller, onVolver: () -> Unit) {
    var paradas by remember { mutableStateOf(controller.obtenerParadas()) }
    var textoBusqueda by remember { mutableStateOf("") }

    // Filtra las paradas según el texto ingresado
    val paradasFiltradas = paradas.filter {
        it.piloto.nombre.contains(textoBusqueda, ignoreCase = true) ||
                it.escuderia.nombre.contains(textoBusqueda, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Título principal
        Text(
            text = "Listado de Pit Stops",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(Modifier.height(8.dp))

        // Campo de búsqueda
        OutlinedTextField(
            value = textoBusqueda,
            onValueChange = { textoBusqueda = it },
            label = { Text("Buscar") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        if (paradasFiltradas.isEmpty()) {
            Text("No hay paradas que coincidan con la búsqueda.")
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(paradasFiltradas) { index, parada ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Piloto: ${parada.piloto.nombre}", fontWeight = FontWeight.Bold)
                            Text("Escudería: ${parada.escuderia.nombre}")
                            Text("Tiempo: ${parada.tiempoSegundos}s")
                            Text("Estado: ${parada.estado.tipoEstado}")
                            parada.observacion?.let { Text("Observación: $it") }
                            Text("Mecánico: ${parada.mecanico.nombre}")
                            Text("Fecha: ${parada.fecha}")

                            Spacer(Modifier.height(8.dp))

                            // Botón eliminar
                            Button(
                                onClick = {
                                    val indexReal = paradas.indexOf(parada)
                                    controller.eliminarParada(indexReal)
                                    paradas = controller.obtenerParadas()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = Color.White
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Eliminar", color = Color.White)
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Botón para volver
        Button(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
            Text("Volver al menú")
        }
    }
}