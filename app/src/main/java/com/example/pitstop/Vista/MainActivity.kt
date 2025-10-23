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
            onResumen = { pantallaActual = "resumen" }
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
    onLista: () -> Unit,
    onResumen: () -> Unit
) {
    var paradas by remember { mutableStateOf(controller.obtenerParadas()) }

    // Recalcular automáticamente cuando cambien los datos
    LaunchedEffect(Unit) {
        paradas = controller.obtenerParadas()
    }

    val totalParadas = paradas.size
    val promedioTiempo = if (paradas.isNotEmpty()) paradas.map { it.tiempoSegundos }.average() else 0.0
    val paradaMasRapida = paradas.minByOrNull { it.tiempoSegundos }
    val ultimaParada = paradas.lastOrNull()
    val escuderiaMasFrecuente = paradas.groupBy { it.escuderia.nombre }
        .maxByOrNull { it.value.size }?.key

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

        if (totalParadas == 0) {
            Text("Aún no hay registros de Pit Stops.", fontSize = 18.sp)
        } else {
            // Lista de tarjetas con cada dato
            InfoCard(
                titulo = "📊 Total de Paradas",
                valor = "$totalParadas"
            )

            InfoCard(
                titulo = "⏱️ Promedio General",
                valor = "%.2f segundos".format(promedioTiempo)
            )

            paradaMasRapida?.let {
                InfoCard(
                    titulo = "🏆 Pit Stop más rápido",
                    valor = "${it.piloto.nombre} – ${it.tiempoSegundos}s"
                )
            }

            escuderiaMasFrecuente?.let {
                InfoCard(
                    titulo = "🚗 Escudería más activa",
                    valor = it
                )
            }

            ultimaParada?.let {
                InfoCard(
                    titulo = "🧑‍🔧 Último Registro",
                    valor = "${it.piloto.nombre} – ${it.fecha}"
                )
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
