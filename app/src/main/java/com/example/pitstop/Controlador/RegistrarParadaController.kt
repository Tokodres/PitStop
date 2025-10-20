package com.example.pitstop.controlador

class RegistrarParadaController {

    fun getPilotos(): List<String> {
        return listOf("Hamilton", "Verstappen", "Leclerc", "Sainz", "Russell")
    }

    fun getEscuderias(): List<String> {
        return listOf("Mercedes", "Red Bull", "Ferrari", "McLaren", "Alpine")
    }

    fun getNeumaticos(): List<String> {
        return listOf("Soft", "Medium", "Hard", "Rain")
    }

    fun getEstados(): List<String> {
        return listOf("Ok", "Mal")
    }
}
