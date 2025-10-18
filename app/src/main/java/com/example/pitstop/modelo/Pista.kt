package com.example.pitstop.modelo
import com.example.pitstop.controlador.PistaController
import java.time.LocalDateTime

data class Pista(
    val piloto: Piloto,
    val escuderia: Escuderia,
    val tiempoSegundos: Int,
    val neumatico: Neumatico,
    val estado: Estado,
    val motivoFallo: String?,
    val mecanico: Mecanico,
    val fecha: Long            // 🔸 antes era LocalDateTime
)

