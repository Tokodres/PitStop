package com.example.pitstop.controlador

import android.content.Context
import com.example.pitstop.modelo.Parada
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/*

1. guardarParada → Crea o agrega una nueva parada.

2. obtenerParadas → Lee todas las paradas.

3. eliminarParada → Borra una parada específica.

4. limpiarParadas → Borra todas las paradas.

5. buscarParadaPorPiloto → Permite consultar rápidamente un piloto específico.

6. obtenerResumen → Da información estadística de las paradas registradas

*/

class PitStop_Controller(private val context: Context) {

    // Objeto SharedPreferences para que nuestros datos no se pierdan cuando acabe
    private val sharedPreferences = context.getSharedPreferences("pitstop_data", Context.MODE_PRIVATE)
    // Gson para convertir objetos Kotlin a JSON  como string
    private val gson = Gson()

    fun guardarParada(parada: Parada) {
        // Obtiene la lista actual de paradas
        val lista = obtenerParadas().toMutableList()
        // Añade la nueva parada
        lista.add(parada)
        // Convierte la lista a JSON
        val json = gson.toJson(lista)
        // Guarda el JSON en SharedPreferences
        sharedPreferences.edit().putString("paradas", json).apply()
    }

    /**
     * Obtiene la lista completa de paradas almacenadas.
     * @return Lista de objetos Parada. Si no hay registros, devuelve una lista vacía. - convierte de json a liosta
     */
    fun obtenerParadas(): List<Parada> {
        val json = sharedPreferences.getString("paradas", null)
        return if (json != null) {
            // Convierte el JSON a lista de objetos Parada
            val tipo = object : TypeToken<List<Parada>>() {}.type
            gson.fromJson(json, tipo)
        } else {
            emptyList()
        }
    }

    /**
     * Elimina una parada de la lista según su índice.
     * @param indice Posición de la parada a eliminar en la lista
     */
    fun eliminarParada(indice: Int) {
        val lista = obtenerParadas().toMutableList()
        // Verifica que el índice sea válido
        if (indice in lista.indices) {
            lista.removeAt(indice)
            val json = gson.toJson(lista)
            sharedPreferences.edit().putString("paradas", json).apply()
        }
    }

    /**
     * Limpia todas las paradas almacenadas.
     */
    fun limpiarParadas() {
        sharedPreferences.edit().remove("paradas").apply()
    }

    /**
     * Busca una parada por el nombre del piloto.
     *  nombrePiloto Nombre del piloto a buscar
     * @return Objeto Parada si existe, o null si no se encuentra
     */
    fun buscarParadaPorPiloto(nombrePiloto: String): Parada? {
        return obtenerParadas().find { it.piloto.nombre.equals(nombrePiloto, ignoreCase = true) }
    }

    /**
     * Genera un resumen de todas las paradas registradas.
     * @return Texto con el total de paradas y el promedio de tiempo en segundos
     */
    fun obtenerResumen(): Map<String, String> {
        val lista = obtenerParadas()
        if (lista.isEmpty()) return mapOf("mensaje" to "No hay registros disponibles.")

        val totalParadas = lista.size
        val promedioTiempo = lista.map { it.tiempoSegundos }.average()
        val paradaMasRapida = lista.minByOrNull { it.tiempoSegundos }
        val ultimaParada = lista.lastOrNull()
        val escuderiaMasFrecuente = lista.groupBy { it.escuderia.nombre }
            .maxByOrNull { it.value.size }?.key ?: "N/A"

        val resumenMap = mutableMapOf<String, String>()
        resumenMap["Total paradas"] = "$totalParadas"
        resumenMap["Promedio tiempo"] = "%.2f segundos".format(promedioTiempo)
        paradaMasRapida?.let {
            resumenMap["Pit Stop más rápido"] = "${it.piloto.nombre} – ${it.tiempoSegundos}s"
        }
        resumenMap["Escudería más activa"] = escuderiaMasFrecuente
        ultimaParada?.let {
            resumenMap["Último registro"] = "${it.piloto.nombre} – ${it.fecha}"
        }

        return resumenMap
    }

}