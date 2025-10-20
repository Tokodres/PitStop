package com.example.pitstop.controlador

import android.content.Context
import android.content.SharedPreferences
import com.example.pitstop.modelo.Parada
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PitStop_Controller(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("pitstop_prefs", Context.MODE_PRIVATE)

    private val gson = Gson()

    fun guardarParada(parada: Parada) {
        val lista = obtenerParadas().toMutableList()
        lista.add(parada)
        val json = gson.toJson(lista)
        prefs.edit().putString("paradas", json).apply()
    }

    fun obtenerParadas(): List<Parada> {
        val json = prefs.getString("paradas", null)
        if (json != null) {
            val type = object : TypeToken<List<Parada>>() {}.type
            return gson.fromJson(json, type)
        }
        return emptyList()
    }

    fun limpiarDatos() {
        prefs.edit().clear().apply()
    }
}
