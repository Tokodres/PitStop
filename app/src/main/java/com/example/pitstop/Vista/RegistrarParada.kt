package com.example.pitstop.vista

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pitstop.controlador.PitStop_Controller
import com.example.pitstop.modelo.*
import java.time.LocalDateTime

@Composable
fun RegistrarParada(onVolver: () -> Unit, context: Context) {
    val controller = remember { PitStop_Controller(context) }

    var piloto by remember { mutableStateOf("") }
    var escuderia by remember { mutableStateOf("") }
    var tiempoSegundos by remember { mutableStateOf("") }
    var neumatico by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("") }
    var motivoFallo by remember { mutableStateOf("") }
    var mecanico by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Registrar Parada", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(value = piloto, onValueChange = { piloto = it }, label = { Text("Piloto") })
        OutlinedTextField(value = escuderia, onValueChange = { escuderia = it }, label = { Text("Escudería") })
        OutlinedTextField(value = tiempoSegundos, onValueChange = { tiempoSegundos = it }, label = { Text("Tiempo (seg)") })
        OutlinedTextField(value = neumatico, onValueChange = { neumatico = it }, label = { Text("Neumático") })
        OutlinedTextField(value = cantidad, onValueChange = { cantidad = it }, label = { Text("Cantidad") })
        OutlinedTextField(value = estado, onValueChange = { estado = it }, label = { Text("Estado") })
        OutlinedTextField(value = motivoFallo, onValueChange = { motivoFallo = it }, label = { Text("Motivo de fallo") })
        OutlinedTextField(value = mecanico, onValueChange = { mecanico = it }, label = { Text("Mecánico") })

        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            if (piloto.isNotBlank() && escuderia.isNotBlank()) {
                val parada = Parada(
                    piloto = Piloto(piloto),
                    escuderia = Escuderia(escuderia),
                    tiempoSegundos = tiempoSegundos.toIntOrNull() ?: 0,
                    neumatico = Neumatico(neumatico, cantidad.toIntOrNull() ?: 0),
                    estado = Estado(estado),
                    motivoFallo = if (motivoFallo.isBlank()) null else motivoFallo,
                    mecanico = Mecanico(mecanico),
                    fechaHora = LocalDateTime.now()
                )
                controller.guardarParada(parada)
            }
        }) {
            Text("Guardar Parada")
        }

        Spacer(Modifier.height(16.dp))
        Button(onClick = onVolver) { Text("Volver al menú") }
    }
}
