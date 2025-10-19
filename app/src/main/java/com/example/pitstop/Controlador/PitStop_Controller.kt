package com.example.pitstop.controlador

import com.example.pitstop.modelo.Parada
import kotlin.math.roundToInt

class PitStopController {

    private val listaParadas = mutableListOf<Parada>()

    fun registrarParada(parada: Parada) {
        listaParadas.add(parada)
    }

    fun eliminarParada(index: Int) {
        if (index in listaParadas.indices) {
            listaParadas.removeAt(index)
        }
    }

    fun obtenerParadas(): List<Parada> = listaParadas

    fun buscarParadas(nombrePiloto: String): List<Parada> {
        return listaParadas.filter {
            it.piloto.nombre.contains(nombrePiloto, ignoreCase = true)
        }
    }

    fun obtenerResumen(): Triple<Double, Double, Int> {
        if (listaParadas.isEmpty()) return Triple(0.0, 0.0, 0)
        val tiempos = listaParadas.map { it.tiempoSegundos }
        val masRapido = tiempos.minOrNull()?.toDouble() ?: 0.0
        val promedio = (tiempos.average() * 100.0).roundToInt() / 100.0
        val total = listaParadas.size
        return Triple(masRapido, promedio, total)
    }
}
