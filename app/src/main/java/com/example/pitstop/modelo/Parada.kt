package com.example.pitstop.modelo

data class Parada(
    val piloto: Piloto,
    val escuderia: Escuderia,
    val tiempoSegundos: Int,
    val neumatico: Neumatico,
    val estado: Estado,
    val observacion: String?,       // puede ser nula
    val mecanico: Mecanico,
    val fecha: String               // formato "dd/MM/yyyy HH:mm"
)
