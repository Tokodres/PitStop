package com.example.pitstop.controlador

import android.content.Context
import com.example.pitstop.modelo.Parada
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PitStop_Controller(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("pitstop_data", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun guardarParada(parada: Parada) {
        val lista = obtenerParadas().toMutableList()
        lista.add(parada)
        val json = gson.toJson(lista)
        sharedPreferences.edit().putString("paradas", json).apply()
    }

    fun obtenerParadas(): List<Parada> {
        val json = sharedPreferences.getString("paradas", null)
        return if (json != null) {
            val tipo = object : TypeToken<List<Parada>>() {}.type
            gson.fromJson(json, tipo)
        } else {
            emptyList()
        }
    }

    fun limpiarParadas() {
        sharedPreferences.edit().remove("paradas").apply()
    }
}
