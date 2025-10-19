package com.example.pitstop.vista

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PantallaInicio(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Bienvenido a PitStop", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(40.dp))
        Button(onClick = { navController.navigate("registro") }) {
            Text("Ir al Registro")
        }
    }
}
