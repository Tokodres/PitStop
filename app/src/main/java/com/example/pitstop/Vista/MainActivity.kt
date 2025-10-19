package com.example.pitstop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pitstop.controlador.PitStopController
import com.example.pitstop.theme.PitStopTheme
import com.example.pitstop.vista.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val controlador = PitStopController()

        setContent {
            PitStopTheme {
                NavegacionPitStop(controlador)
            }
        }
    }
}

@Composable
fun NavegacionPitStop(controlador: PitStopController) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "inicio") {
        composable("inicio") { PantallaResumen(navController, controlador) }
        composable("listado") { PantallaListado(navController, controlador) }
        composable("registro") { PantallaRegistro(navController, controlador) }
    }
}
