package com.example.pitstop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pitstop.controlador.PitStop_Controller
import com.example.pitstop.modelo.Parada
import com.example.pitstop.vista.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val controller = PitStop_Controller(this)

        setContent {

            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FondoPitStop()
                    PitStopApp(controller)
                }
            }
        }
    }
}

@Composable
fun PitStopApp(controller: PitStop_Controller) {
    var pantallaActual by remember { mutableStateOf("menu") }

    when (pantallaActual) {
        "menu" -> DashboardPitStop(
            controller = controller,
            onRegistrar = { pantallaActual = "registrar" },
            onLista = { pantallaActual = "lista" },

            )

        "registrar" -> RegistrarParadaScreen(
            controller = controller,
            onVolver = { pantallaActual = "menu" }
        )

        "lista" -> ListaParadasScreen(
            controller = controller,
            onVolver = { pantallaActual = "menu" }
        )
    }
}

@Composable
fun DashboardPitStop(
    controller: PitStop_Controller,
    onRegistrar: () -> Unit,
    onLista: () -> Unit
) {
    // Obtenemos el resumen completo desde el controlador
    val resumen = controller.obtenerResumen()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🏁 PIT STOP STATS",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (resumen.containsKey("mensaje")) {
            Text(
                text = resumen["mensaje"] ?: "",
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
            )
        } else {
            resumen.forEach { (titulo, valor) ->
                val titulo1 = when(titulo) {
                    "Total paradas" -> "📊 $titulo"
                    "Promedio tiempo" -> "⏱ $titulo"
                    "Pit Stop más rápido" -> "🏆 $titulo"
                    "Último registro" -> "🧑‍🔧 $titulo"
                    "Escudería más activa" -> "🚗 $titulo"
                    else -> titulo
                }
                InfoCard(titulo = titulo1, valor = valor)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botones de navegación
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onRegistrar, modifier = Modifier.fillMaxWidth()) {
                Text("Registrar Nueva Parada")
            }

            Button(onClick = onLista, modifier = Modifier.fillMaxWidth()) {
                Text("Ver Lista Completa")
            }
        }
    }
}

@Composable
fun InfoCard(titulo: String, valor: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = valor,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
            )
        }
    }
}