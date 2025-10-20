package com.example.pitstop.vista

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pitstop.controlador.PitStop_Controller
import com.example.pitstop.modelo.*

@Composable
fun RegistrarParadaScreen(controller: PitStop_Controller, onVolver: () -> Unit) {
    // Variables de estado
    var nombrePiloto by remember { mutableStateOf("") }
    var nombreMecanico by remember { mutableStateOf("") }
    var nombreEscuderia by remember { mutableStateOf("") }
    var tipoNeumatico by remember { mutableStateOf("") }
    var cantidadNeumaticos by remember { mutableStateOf("") }
    var tipoEstado by remember { mutableStateOf("") }
    var observacion by remember { mutableStateOf("") }
    var tiempoSegundos by remember { mutableStateOf("") }
    var fechaManual by remember { mutableStateOf("") }

    // Contenedor principal centrado
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título principal
        Text(
            text = "Registrar Pit Stop",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(Modifier.height(20.dp))

        // Campos de entrada
        OutlinedTextField(
            value = nombrePiloto,
            onValueChange = { nombrePiloto = it },
            label = { Text("Piloto") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = nombreEscuderia,
            onValueChange = { nombreEscuderia = it },
            label = { Text("Escudería") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = tiempoSegundos,
            onValueChange = { tiempoSegundos = it },
            label = { Text("Tiempo Total (s)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = tipoNeumatico,
            onValueChange = { tipoNeumatico = it },
            label = { Text("Cambio de Neumáticos") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = cantidadNeumaticos,
            onValueChange = { cantidadNeumaticos = it },
            label = { Text("Número de Neumáticos Cambiados") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = tipoEstado,
            onValueChange = { tipoEstado = it },
            label = { Text("Estado (OK / Fallido)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = observacion,
            onValueChange = { observacion = it },
            label = { Text("Motivo del fallo (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = nombreMecanico,
            onValueChange = { nombreMecanico = it },
            label = { Text("Mecánico Principal") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = fechaManual,
            onValueChange = { fechaManual = it },
            label = { Text("Fecha y hora del Pit Stop (dd/MM/yyyy HH:mm)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        // Fila de botones
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Botón GUARDAR (rojo)
            Button(
                onClick = {
                    if (nombrePiloto.isNotBlank() && fechaManual.isNotBlank()) {
                        val parada = Parada(
                            piloto = Piloto(nombrePiloto),
                            escuderia = Escuderia(nombreEscuderia),
                            tiempoSegundos = tiempoSegundos.toIntOrNull() ?: 0,
                            neumatico = Neumatico(
                                tipo = tipoNeumatico,
                                cantidad = cantidadNeumaticos.toIntOrNull() ?: 0
                            ),
                            estado = Estado(tipoEstado),
                            observacion = if (observacion.isBlank()) null else observacion,
                            mecanico = Mecanico(nombreMecanico),
                            fecha = fechaManual
                        )

                        controller.guardarParada(parada)
                        onVolver()
                    }
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("GUARDAR", color = Color.White, fontWeight = FontWeight.Bold)
            }

            // Botón CANCELAR (negro)
            Button(
                onClick = onVolver,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("CANCELAR", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
