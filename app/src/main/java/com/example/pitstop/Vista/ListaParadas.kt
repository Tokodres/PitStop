package com.example.pitstop.vista

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pitstop.controlador.PitStop_Controller
import com.example.pitstop.modelo.Parada

@Composable
fun ListaParadas(onVolver: () -> Unit, context: Context) {
    val controller = remember { PitStop_Controller(context) }
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
                items(paradas) { parada ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("Piloto: ${parada.piloto.nombre}")
                            Text("Escudería: ${parada.escuderia.nombre}")
                            Text("Tiempo: ${parada.tiempoSegundos} seg")
                            Text("Neumático: ${parada.neumatico.nombre} (${parada.neumatico.cantidad})")
                            Text("Estado: ${parada.estado.tipoEstado}")
                            parada.motivoFallo?.let { Text("Fallo: $it") }
                            Text("Mecánico: ${parada.mecanico.nombre}")
                            Text("Fecha: ${parada.fechaHora}")
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
