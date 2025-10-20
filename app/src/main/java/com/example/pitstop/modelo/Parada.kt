package com.example.pitstop.modelo

import java.time.LocalDateTime

data class Parada(
    val piloto: Piloto,
    val escuderia: Escuderia,
    val tiempoSegundos: Int,
    val neumatico: Neumatico,
    val estado: Estado,
    val motivoFallo: String?,
    val mecanico: Mecanico,
    val fechaHora: String // formato "dd/MM/yyyy HH:mm"
)
