package com.example.pitstop.vista

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pitstop.controlador.PitStopController
import com.example.pitstop.modelo.*
import java.time.LocalDateTime

@Composable
fun PantallaRegistro(navController: NavController, controlador: PitStopController) {
    var piloto by remember { mutableStateOf("") }
    var escuderia by remember { mutableStateOf("") }
    var tiempo by remember { mutableStateOf("") }
    var neumatico by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("") }
    var mecanico by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registrar Pit Stop", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(value = piloto, onValueChange = { piloto = it }, label = { Text("Piloto") })
        OutlinedTextField(value = escuderia, onValueChange = { escuderia = it }, label = { Text("Escudería") })
        OutlinedTextField(value = tiempo, onValueChange = { tiempo = it }, label = { Text("Tiempo (s)") })
        OutlinedTextField(value = neumatico, onValueChange = { neumatico = it }, label = { Text("Tipo neumático") })
        OutlinedTextField(value = cantidad, onValueChange = { cantidad = it }, label = { Text("Cantidad cambiada") })
        OutlinedTextField(value = estado, onValueChange = { estado = it }, label = { Text("Estado") })
        OutlinedTextField(value = motivo, onValueChange = { motivo = it }, label = { Text("Motivo del fallo") })
        OutlinedTextField(value = mecanico, onValueChange = { mecanico = it }, label = { Text("Mecánico principal") })

        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            val parada = Parada(
                Piloto(piloto),
                Escuderia(escuderia),
                tiempo.toIntOrNull() ?: 0,
                Neumatico(neumatico, cantidad.toIntOrNull() ?: 0),
                Estado(estado),
                motivo.ifEmpty { null },
                Mecanico(mecanico),
                LocalDateTime.now()
            )
            controlador.registrarParada(parada)
            navController.navigate("inicio")
        }) {
            Text("Guardar")
        }
    }
}
