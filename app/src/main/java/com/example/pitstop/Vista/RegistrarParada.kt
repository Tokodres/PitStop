package com.example.pitstop.vista

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pitstop.controlador.PitStop_Controller
import com.example.pitstop.controlador.RegistrarParadaController
import com.example.pitstop.modelo.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown


@Composable
fun RegistrarParada(onVolver: () -> Unit, context: Context) {
    val controller = remember { PitStop_Controller(context) }
    val registrarController = remember { RegistrarParadaController() }

    // --- Obtener opciones desde el controlador de registro ---
    val pilotosOpciones = registrarController.getPilotos()
    val escuderiasOpciones = registrarController.getEscuderias()
    val neumaticoOpciones = registrarController.getNeumaticos()
    val estadoOpciones = registrarController.getEstados()

    // --- Estados de los campos ---
    var piloto by remember { mutableStateOf(pilotosOpciones.first()) }
    var escuderia by remember { mutableStateOf(escuderiasOpciones.first()) }
    var neumatico by remember { mutableStateOf(neumaticoOpciones.first()) }
    var cantidadNeumaticos by remember { mutableStateOf("0") }
    var estado by remember { mutableStateOf(estadoOpciones.first()) }
    var motivoFallo by remember { mutableStateOf("") }
    var mecanico by remember { mutableStateOf("") }
    var tiempoSegundos by remember { mutableStateOf("") }
    var fechaHora by remember { mutableStateOf("") } // Editable por el usuario

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text("Registrar Pit Stop", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))

        // --- Dropdown de Piloto ---
        Text("Piloto")
        DropdownSelector(pilotosOpciones, piloto) { piloto = it }

        // --- Dropdown de Escudería ---
        Text("Escudería")
        DropdownSelector(escuderiasOpciones, escuderia) { escuderia = it }

        // --- Tiempo en segundos ---
        OutlinedTextField(
            value = tiempoSegundos,
            onValueChange = { tiempoSegundos = it },
            label = { Text("Tiempo (segundos)") },
            modifier = Modifier.fillMaxWidth()
        )

        // --- Dropdown de Neumático ---
        Text("Cambio de neumático")
        DropdownSelector(neumaticoOpciones, neumatico) { neumatico = it }

        // --- Cantidad de neumáticos ---
        OutlinedTextField(
            value = cantidadNeumaticos,
            onValueChange = { cantidadNeumaticos = it },
            label = { Text("Cantidad de neumáticos") },
            modifier = Modifier.fillMaxWidth()
        )

        // --- Dropdown de Estado ---
        Text("Estado")
        DropdownSelector(estadoOpciones, estado) { estado = it }

        // --- Motivo de fallo ---
        OutlinedTextField(
            value = motivoFallo,
            onValueChange = { motivoFallo = it },
            label = { Text("Motivo de fallo (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )

        // --- Mecánico ---
        OutlinedTextField(
            value = mecanico,
            onValueChange = { mecanico = it },
            label = { Text("Mecánico") },
            modifier = Modifier.fillMaxWidth()
        )

        // --- Fecha y hora editable ---
        OutlinedTextField(
            value = fechaHora,
            onValueChange = { fechaHora = it },
            label = { Text("Fecha y hora (dd/MM/yyyy HH:mm)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                if (piloto.isNotBlank() && escuderia.isNotBlank() && fechaHora.isNotBlank()) {
                    val parada = Parada(
                        piloto = Piloto(piloto),
                        escuderia = Escuderia(escuderia),
                        tiempoSegundos = tiempoSegundos.toIntOrNull() ?: 0,
                        neumatico = Neumatico(neumatico, cantidadNeumaticos.toIntOrNull() ?: 0),
                        estado = Estado(estado),
                        motivoFallo = if (motivoFallo.isBlank()) null else motivoFallo,
                        mecanico = Mecanico(mecanico),
                        fechaHora = fechaHora
                    )
                    controller.guardarParada(parada)
                    onVolver()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Parada")
        }

        Spacer(Modifier.height(12.dp))
        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al menú")
        }
    }
}

@Composable
fun DropdownSelector(opciones: List<String>, seleccionActual: String, onSeleccion: (String) -> Unit) {
    var expandir by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = seleccionActual,
            onValueChange = {},
            label = { Text("Seleccionar") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { expandir = !expandir }) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }
        )
        DropdownMenu(
            expanded = expandir,
            onDismissRequest = { expandir = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onSeleccion(opcion)
                        expandir = false
                    }
                )
            }
        }
    }
}
