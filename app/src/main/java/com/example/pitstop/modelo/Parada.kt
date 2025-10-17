package com.example.pitstop.modelo
import java.time.LocalDateTime

class Parada(
    val piloto: Piloto,
    val escuderia: Escuderia,
    val tiempoSegundos: Int,
    val neumatico: Neumatico,
    val estado: Estado,
    val motivoFallo: String?,
    val mecanico: Mecanico,
    val fechaHora: LocalDateTime
)