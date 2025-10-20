package com.example.pitstop.vista

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pitstop.controlador.PitStop_Controller
import com.example.pitstop.modelo.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val controller = PitStop_Controller(this)

        setContent {
            PitStopApp(controller)
        }
    }
}

@Composable
fun PitStopApp(controller: PitStop_Controller) {
    var pantalla by remember { mutableStateOf("menu") }

    when (pantalla) {
        "menu" -> MenuScreen(
            onRegistrar = { pantalla = "registro" },
            onListar = { pantalla = "listado" }
        )
        "registro" -> RegistrarParadaScreen(
            controller = controller,
            onVolver = { pantalla = "menu" }
        )
        "listado" -> ListaParadasScreen(
            controller = controller,
            onVolver = { pantalla = "menu" }
        )
    }
}

@Composable
fun MenuScreen(onRegistrar: () -> Unit, onListar: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("PitStop — Menú Principal", fontSize = 22.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = onRegistrar, modifier = Modifier.fillMaxWidth()) {
            Text("Registrar parada")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = onListar, modifier = Modifier.fillMaxWidth()) {
            Text("Ver listado de paradas")
        }
    }
}

@Composable
fun RegistrarParadaScreen(controller: PitStop_Controller, onVolver: () -> Unit) {
    var nombrePiloto by remember { mutableStateOf("") }
    var nombreMecanico by remember { mutableStateOf("") }
    var nombreEscuderia by remember { mutableStateOf("") }
    var tipoNeumatico by remember { mutableStateOf("") }
    var cantidadNeumaticos by remember { mutableStateOf("") }
    var tipoEstado by remember { mutableStateOf("") }
    var motivoFallo by remember { mutableStateOf("") }
    var tiempoSegundos by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registrar Pit Stop", fontSize = 22.sp)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(value = nombrePiloto, onValueChange = { nombrePiloto = it }, label = { Text("Piloto") })
        OutlinedTextField(value = nombreMecanico, onValueChange = { nombreMecanico = it }, label = { Text("Mecánico") })
        OutlinedTextField(value = nombreEscuderia, onValueChange = { nombreEscuderia = it }, label = { Text("Escudería") })
        OutlinedTextField(value = tipoNeumatico, onValueChange = { tipoNeumatico = it }, label = { Text("Neumático") })
        OutlinedTextField(value = cantidadNeumaticos, onValueChange = { cantidadNeumaticos = it }, label = { Text("Cantidad neumáticos") })
        OutlinedTextField(value = tipoEstado, onValueChange = { tipoEstado = it }, label = { Text("Estado") })
        OutlinedTextField(value = motivoFallo, onValueChange = { motivoFallo = it }, label = { Text("Motivo de fallo (opcional)") })
        OutlinedTextField(value = tiempoSegundos, onValueChange = { tiempoSegundos = it }, label = { Text("Tiempo (segundos)") })

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (nombrePiloto.isNotBlank() && nombreMecanico.isNotBlank()) {
                val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val fechaActual = formato.format(Date())

                val parada = Parada(
                    piloto = Piloto(nombrePiloto),
                    escuderia = Escuderia(nombreEscuderia),
                    tiempoSegundos = tiempoSegundos.toIntOrNull() ?: 0,
                    neumatico = Neumatico(tipoNeumatico, cantidadNeumaticos.toIntOrNull() ?: 0),
                    estado = Estado(tipoEstado),
                    motivoFallo = if (motivoFallo.isBlank()) null else motivoFallo,
                    mecanico = Mecanico(nombreMecanico),
                    fechaHora = fechaActual
                )
                controller.guardarParada(parada)
                onVolver()
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Guardar registro")
        }

        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = onVolver, modifier = Modifier.fillMaxWidth()) {
            Text("Volver al menú")
        }
    }
}

@Composable
fun ListaParadasScreen(controller: PitStop_Controller, onVolver: () -> Unit) {
    val paradas = remember { mutableStateListOf<Parada>() }

    LaunchedEffect(Unit) {
        paradas.clear()
        paradas.addAll(controller.obtenerParadas())
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Listado de Paradas", fontSize = 22.sp)
        Spacer(modifier = Modifier.height(10.dp))

        if (paradas.isEmpty()) {
            Text("No hay paradas registradas.")
        } else {
            LazyColumn {
                items(paradas) { p ->
                    Card(modifier = Modifier.fillMaxWidth().padding(6.dp)) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("Piloto: ${p.piloto.nombre}")
                            Text("Mecánico: ${p.mecanico.nombre}")
                            Text("Escudería: ${p.escuderia.nombre}")
                            Text("Tiempo: ${p.tiempoSegundos}s")
                            Text("Neumático: ${p.neumatico.nombre} (${p.neumatico.cantidad})")
                            Text("Estado: ${p.estado.tipoEstado}")
                            if (!p.motivoFallo.isNullOrBlank())
                                Text("Fallo: ${p.motivoFallo}")
                            Text("Fecha: ${p.fechaHora}")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onVolver, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Volver al Menú")
        }
    }
}
