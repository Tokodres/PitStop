package com.example.pitstop.vista

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuPrincipal(onNavegarRegistrar: () -> Unit, onNavegarLista: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("PitStop — Menú Principal", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(32.dp))
        Button(onClick = onNavegarRegistrar, modifier = Modifier.fillMaxWidth()) {
            Text("Registrar Parada")
        }
        Spacer(Modifier.height(16.dp))
        Button(onClick = onNavegarLista, modifier = Modifier.fillMaxWidth()) {
            Text("Ver Listado de Paradas")
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var pantalla by remember { mutableStateOf("menu") }

            when (pantalla) {
                "menu" -> MenuPrincipal(
                    onNavegarRegistrar = { pantalla = "registrar" },
                    onNavegarLista = { pantalla = "lista" }
                )
                "registrar" -> RegistrarParada(onVolver = { pantalla = "menu" }, this)
                "lista" -> ListaParadas(onVolver = { pantalla = "menu" }, this)
            }
        }
    }
}
